package jar.StockValueApp.service;


import jar.StockValueApp.dto.DividendDiscountRequestDTO;
import jar.StockValueApp.dto.DividendDiscountResponseDTO;
import jar.StockValueApp.model.DividendDiscountModel;
import jar.StockValueApp.repository.DividendDiscountRepository;
import jar.StockValueApp.repository.UserRepository;
import jar.StockValueApp.service.mappingService.DividendDiscountMappingService;
import jar.StockValueApp.validator.DividendDiscountRequestValidator;
import jar.StockValueApp.validator.GlobalExceptionValidator;
import jar.StockValueApp.validator.UserRequestValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
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
    public List<DividendDiscountModel> getAllDividendDiscountValuations() {
        final var dividendDiscountValuations = dividendDiscountRepository.findAll();
        dividendDiscountRequestValidator.validateDividendDiscountList(dividendDiscountValuations);

        log.info(dividendDiscountValuations.size() + " Dividend discount valuations were found in DB.");
        return dividendDiscountValuations;
    }

    @Cacheable(value = "dividendDiscountValuationsByTickerCache", key = "#ticker.concat('-').concat(#userId.toString())")
    public ArrayList<Object> getDividendDiscountValuationsByTicker(final String ticker, final Long userId){
        globalExceptionValidator.validateId(userId);
        userRequestValidator.validateUserById(userId);

        final var companiesValuations = dividendDiscountRepository.findByUserId(userId);

        final var filteredCompaniesByTicker = companiesValuations.stream()
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
    public ArrayList<Object> getDividendDiscountValuationsByCompanyName(
            final String companyName,
            final Long userId
    ){
        globalExceptionValidator.validateId(userId);
        userRequestValidator.validateUserById(userId);

        final var companiesValuations = dividendDiscountRepository.findByUserId(userId);

        final var filteredCompaniesByCompanyName = companiesValuations.stream()
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
    public ArrayList<Object> getDividendDiscountValuationsByDate(
            final LocalDate startDate,
            final LocalDate endDate,
            final Long userId
   ){
        globalExceptionValidator.validateId(userId);
        userRequestValidator.validateUserById(userId);

        final var companiesValuations = dividendDiscountRepository.findByUserId(userId);

        final var filteredCompaniesByDate = companiesValuations.stream()
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
    ) {
        globalExceptionValidator.validateId(userId);
        userRequestValidator.validateUserById(userId);
        dividendDiscountRequestValidator.validateDividendDiscountRequest(dividendDiscountRequestDTO);
        dividendDiscountRequestValidator.validateExpectedGrowthRateInput(
                dividendDiscountRequestDTO.getWacc(),
                dividendDiscountRequestDTO.getExpectedGrowthRate()
        );
        final var user = userRepository.getReferenceById(userId);

        final var dividendDiscountModel = dividendDiscountMappingService.mapToEntity(dividendDiscountRequestDTO);
        user.getDividendDiscountModels().add(dividendDiscountModel);
        dividendDiscountModel.setUser(user);

        cacheService.evictAllDividendDiscountValuationsCaches();
        dividendDiscountRepository.save(dividendDiscountModel);
        log.info("Calculation created successfully.");
        return dividendDiscountMappingService.mapToResponse(dividendDiscountModel);
    }

    public HashMap<Object, Object> deleteDividendDiscountValuationById(final Long valuationId, final Long userId){
        globalExceptionValidator.validateId(valuationId);
        globalExceptionValidator.validateId(userId);
        dividendDiscountRequestValidator.validateDividendDiscountById(valuationId);
        dividendDiscountRequestValidator.validateDividendDiscountModelForUser(valuationId, userId);

        cacheService.evictAllDividendDiscountValuationsCaches();
        dividendDiscountRepository.deleteById(valuationId);
        log.info("Dividend discount valuation  with id number " + valuationId + " was deleted from DB successfully.");

        return createDeletionResponse(valuationId);
    }

    private HashMap<Object, Object> createDeletionResponse(final Long valuationId){
        var response = new HashMap<>();
        response.put("message", "Dividend discount valuation  with id number " + valuationId + " was deleted from DB successfully.");
        return response;
    }
}
