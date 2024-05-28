package jar.StockValueApp.service;


import jar.StockValueApp.dto.GrahamsRequestDTO;
import jar.StockValueApp.dto.GrahamsResponseDTO;
import jar.StockValueApp.exception.*;
import jar.StockValueApp.model.GrahamsModel;
import jar.StockValueApp.model.User;
import jar.StockValueApp.repository.GrahamsModelRepository;
import jar.StockValueApp.repository.UserRepository;
import jar.StockValueApp.service.mappingService.GrahamsModelMappingService;
import jar.StockValueApp.validator.GlobalExceptionValidator;
import jar.StockValueApp.validator.GrahamsModelRequestValidator;
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
public class GrahamsModelService {

    private final GrahamsModelRepository grahamsRepository;
    private final GrahamsModelMappingService grahamsModelMappingService;
    private final GlobalExceptionValidator globalExceptionValidator;
    private final GrahamsModelRequestValidator grahamsModelRequestValidator;
    private final UserRequestValidator userRequestValidator;
    private final UserRepository userRepository;
    private final CacheService cacheService;

    @Cacheable(value = "grahamsValuationsCache")
    public List<GrahamsModel> getAllGrahamsValuations() throws NoGrahamsModelFoundException {
        final List<GrahamsModel> grahamsValuations = grahamsRepository.findAll();
        grahamsModelRequestValidator.validateGrahamsModelList(grahamsValuations);

        log.info(grahamsValuations.size() + " Grahams valuations were found in DB.");
        return grahamsValuations;
    }

    // The key expression #ticker.concat('-').concat(#usersId.toString()) constructs a unique identifier for caching by concatenating the stock ticker symbol (ticker)
// and user ID (usersId), separated by a hyphen. This ensures that cached data is specific to both the stock and the user, preventing cache collisions between
// different stocks or users.
    @Cacheable(value = "grahamsValuationsByTickerCache", key = "#ticker.concat('-').concat(#userId.toString())")
    public List<GrahamsResponseDTO> getGrahamsValuationsByTicker(final String ticker, final Long userId)
            throws NoGrahamsModelFoundException, NotValidIdException, NoUsersFoundException {
        globalExceptionValidator.validateId(userId);
        userRequestValidator.validateUserById(userId);
        final List<GrahamsModel> companiesValuations = grahamsRepository.findByUserId(userId);

        final List<GrahamsModel> filteredCompaniesByTicker = companiesValuations.stream()
                .filter(valuation -> valuation.getTicker().equalsIgnoreCase(ticker))
                .collect(Collectors.toList());

        grahamsModelRequestValidator.validateGrahamsModelList(filteredCompaniesByTicker, ticker);

        log.info("Found " + filteredCompaniesByTicker.size() + " Grahams company valuations with ticker: " + ticker);
        return grahamsModelMappingService.mapToResponse(filteredCompaniesByTicker);
    }

    @Cacheable(value = "grahamsValuationByCompanyNameCache", key = "#companyName.concat('-').concat(#userId.toString())")
    public List<GrahamsResponseDTO> getGrahamsValuationsByCompanyName(final String companyName, final Long userId)
            throws NoGrahamsModelFoundException, NotValidIdException, NoUsersFoundException {
        globalExceptionValidator.validateId(userId);
        userRequestValidator.validateUserById(userId);
        final List<GrahamsModel> companiesValuations = grahamsRepository.findByUserId(userId);

        final List<GrahamsModel> filteredCompaniesByCompanyName = companiesValuations.stream()
                .filter(valuation -> valuation.getName().equalsIgnoreCase(companyName))
                .collect(Collectors.toList());

        grahamsModelRequestValidator.validateGrahamsModelList(filteredCompaniesByCompanyName, companyName);

        log.info("Found " + filteredCompaniesByCompanyName.size()
                + " Grahams company valuations with ticker: " + companyName);
        return grahamsModelMappingService.mapToResponse(filteredCompaniesByCompanyName);
    }

    @Cacheable(
            value = "grahamsValuationsByDateCache",
            key = "#startDate.toString().concat('-').concat(#endDate.toString()).concat('-').concat(#userId.toString())")
    public List<GrahamsResponseDTO> getGrahamsValuationsByDate(
            final LocalDate startDate,
            final LocalDate endDate,
            final Long userId
    ) throws NoGrahamsModelFoundException, NotValidIdException, NoUsersFoundException {
        globalExceptionValidator.validateId(userId);
        userRequestValidator.validateUserById(userId);
        final List<GrahamsModel> companiesValuations = grahamsRepository.findByUserId(userId);

        final List<GrahamsModel> filteredValuationsByDate = companiesValuations.stream()
                .filter(valuation ->
                        valuation.getCreationDate().isAfter(startDate.minusDays(1)) &&
                                valuation.getCreationDate().isBefore(endDate.plusDays(1)))
                .collect(Collectors.toList());

        grahamsModelRequestValidator.validateGrahamsModelList(filteredValuationsByDate, startDate, endDate);

        log.info("Found " + filteredValuationsByDate.size()
                + " Grahams valuations made between: " + startDate + " and " + endDate);
        return grahamsModelMappingService.mapToResponse(filteredValuationsByDate);
    }

    public GrahamsResponseDTO addGrahamsValuation(final GrahamsRequestDTO grahamsRequestDTO, final Long userId)
            throws MandatoryFieldsMissingException, NotValidIdException, NoUsersFoundException {
        globalExceptionValidator.validateId(userId);
        userRequestValidator.validateUserById(userId);
        grahamsModelRequestValidator.validateGrahamsModelRequest(grahamsRequestDTO);
        final User user = userRepository.getReferenceById(userId);

        final GrahamsModel grahamsModel = grahamsModelMappingService.mapToEntity(grahamsRequestDTO);
        user.getGrahamsModels().add(grahamsModel);
        grahamsModel.setUser(user);

        cacheService.evictAllGrahamsValuationsCaches();
        grahamsRepository.save(grahamsModel);
        log.info("Calculation created successfully.");
        return grahamsModelMappingService.mapToResponse(grahamsModel);
    }

    public void deleteGrahamsValuationById(final Long valuationId, final Long userId)
            throws NotValidIdException, NoGrahamsModelFoundException, ValuationDoestExistForSelectedUserException {
        globalExceptionValidator.validateId(valuationId);
        globalExceptionValidator.validateId(userId);
        grahamsModelRequestValidator.validateGrahamsModelById(valuationId);
        grahamsModelRequestValidator.validateGrahamsModelForUser(valuationId, userId);

        cacheService.evictAllGrahamsValuationsCaches();
        grahamsRepository.deleteById(valuationId);
        log.info("Grahams valuation  with id number " + valuationId + " was deleted from DB successfully.");
    }
}
