package jar.StockValueApp.service;


import jar.StockValueApp.dto.DividendDiscountRequestDTO;
import jar.StockValueApp.dto.DividendDiscountResponseDTO;
import jar.StockValueApp.exception.*;
import jar.StockValueApp.model.DividendDiscountModel;
import jar.StockValueApp.model.User;
import jar.StockValueApp.repository.DividendDiscountRepository;
import jar.StockValueApp.repository.UserRepository;
import jar.StockValueApp.service.calculationService.MathService;
import jar.StockValueApp.service.mappingService.DividendDiscountMappingService;
import jar.StockValueApp.validator.DividendDiscountRequestValidator;
import jar.StockValueApp.validator.GlobalExceptionValidator;
import jar.StockValueApp.validator.UserRequestValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DividendDiscountService {

    private final DividendDiscountRepository dividendDiscountRepository;
    private final DividendDiscountMappingService dividendDiscountMappingService;
    private final DividendDiscountRequestValidator dividendDiscountRequestValidator;
    private final GlobalExceptionValidator globalExceptionValidator;
    private final UserRequestValidator userRequestValidator;
    private final UserRepository userRepository;
    private final CacheService cacheService;

    @Cacheable(value = "dividendDiscountValuationsCache")
    public List<DividendDiscountModel> getAllDividendDiscountValuations() throws NoDividendDiscountModelFoundException {
        final List<DividendDiscountModel> dividendDiscountValuations = dividendDiscountRepository.findAll();
        dividendDiscountRequestValidator.validateDividendDiscountList(dividendDiscountValuations);

        log.info(dividendDiscountValuations.size() + " Dividend discount valuations were found in DB.");
        return dividendDiscountValuations;
    }

    @Cacheable(value = "dividendDiscountValuationsByTickerCache", key = "#ticker.concat('-').concat(#userId.toString())")
    public List<DividendDiscountResponseDTO> getDividendDiscountValuationsByTicker(final String ticker, final Long userId)
            throws NoDividendDiscountModelFoundException, NotValidIdException, NoUsersFoundException {
        globalExceptionValidator.validateId(userId);
        userRequestValidator.validateUserById(userId);

        final List<DividendDiscountModel> companiesValuations = dividendDiscountRepository.findByUserId(userId);

        final List<DividendDiscountModel> filteredCompaniesByTicker = companiesValuations.stream()
                .filter(valuation -> valuation.getTicker().equalsIgnoreCase(ticker))
                .collect(Collectors.toList());

        dividendDiscountRequestValidator.validateDividendDiscountList(filteredCompaniesByTicker, ticker);
        log.info("Found " + filteredCompaniesByTicker.size()
                + " Dividend discount company valuations with ticker: " + ticker);
        return dividendDiscountMappingService.mapToResponse(filteredCompaniesByTicker);
    }

    @Cacheable(
            value = "dividendDiscountValuationsByCompanyNameCache",
            key = "#companyName.concat('-').concat(#userId.toString())"
    )
    public List<DividendDiscountResponseDTO> getDividendDiscountValuationsByCompanyName(
            final String companyName,
            final Long userId
    ) throws NoDividendDiscountModelFoundException, NotValidIdException, NoUsersFoundException {
        globalExceptionValidator.validateId(userId);
        userRequestValidator.validateUserById(userId);

        final List<DividendDiscountModel> companiesValuations = dividendDiscountRepository.findByUserId(userId);

        final List<DividendDiscountModel> filteredCompaniesByCompanyName = companiesValuations.stream()
                .filter(valuation -> valuation.getCompanyName().equalsIgnoreCase(companyName))
                .collect(Collectors.toList());

        dividendDiscountRequestValidator.validateDividendDiscountList(filteredCompaniesByCompanyName, companyName);

        log.info("Found " + filteredCompaniesByCompanyName.size()
                + " Dividend discount company valuations with name: " + companyName);
        return dividendDiscountMappingService.mapToResponse(filteredCompaniesByCompanyName);
    }

   @Cacheable(
           value = "dividendDiscountValuationsByDateCache",
           key = "#startDate.toString().concat('-').concat(#endDate.toString()).concat('-').concat(#userId.toString())")
    public List<DividendDiscountResponseDTO> getDividendDiscountValuationsByDate(
            final LocalDate startDate,
            final LocalDate endDate,
            final Long userId
   ) throws NoDividendDiscountModelFoundException, NotValidIdException, NoUsersFoundException {
        globalExceptionValidator.validateId(userId);
        userRequestValidator.validateUserById(userId);

        final List<DividendDiscountModel> companiesValuations = dividendDiscountRepository.findByUserId(userId);

        final List<DividendDiscountModel> filteredCompaniesByDate = companiesValuations.stream()
                .filter(valuation ->
                        valuation.getCreationDate().isAfter(startDate.minusDays(1)) &&
                                valuation.getCreationDate().isBefore(endDate.plusDays(1)))
                .collect(Collectors.toList());

        dividendDiscountRequestValidator.validateDividendDiscountList(filteredCompaniesByDate, startDate, endDate);

        log.info("Found " + filteredCompaniesByDate.size()
                + " Dividend discount valuations between: " + startDate + " and " + endDate);
        return dividendDiscountMappingService.mapToResponse(filteredCompaniesByDate);
    }

    public DividendDiscountResponseDTO addDividendDiscountValuation(
            final DividendDiscountRequestDTO dividendDiscountRequestDTO,
            final Long userId
    ) throws
            MandatoryFieldsMissingException,
            NotValidIdException,
            NoUsersFoundException,
            IncorrectCompaniesExpectedGrowthException
    {
        globalExceptionValidator.validateId(userId);
        userRequestValidator.validateUserById(userId);
        dividendDiscountRequestValidator.validateDividendDiscountRequest(dividendDiscountRequestDTO);
        dividendDiscountRequestValidator.validateExpectedGrowthRateInput(
                dividendDiscountRequestDTO.getWacc(),
                dividendDiscountRequestDTO.getExpectedGrowthRate()
        );
        final User user = userRepository.getReferenceById(userId);

        final DividendDiscountModel dividendDiscountModel = dividendDiscountMappingService.mapToEntity(dividendDiscountRequestDTO);
        user.getDividendDiscountModels().add(dividendDiscountModel);
        dividendDiscountModel.setUser(user);

        cacheService.evictAllDividendDiscountValuationsCaches();
        dividendDiscountRepository.save(dividendDiscountModel);
        log.info("Calculation created successfully.");
        return dividendDiscountMappingService.mapToResponse(dividendDiscountModel);
    }

    public void deleteDividendDiscountValuationById(final Long valuationId, final Long userId)
            throws
            NotValidIdException,
            NoDividendDiscountModelFoundException,
            ValuationDoestExistForSelectedUserException
    {
        globalExceptionValidator.validateId(valuationId);
        globalExceptionValidator.validateId(userId);
        dividendDiscountRequestValidator.validateDividendDiscountById(valuationId);
        dividendDiscountRequestValidator.validateDividendDiscountModelForUser(valuationId, userId);

        cacheService.evictAllDividendDiscountValuationsCaches();
        dividendDiscountRepository.deleteById(valuationId);
        log.info("Dividend discount valuation  with id number " + valuationId + " was deleted from DB successfully.");
    }
}
