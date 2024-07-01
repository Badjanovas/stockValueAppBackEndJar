package jar.StockValueApp.service;


import jar.StockValueApp.dto.DcfModelRequestDTO;
import jar.StockValueApp.dto.DcfModelResponseDTO;
import jar.StockValueApp.model.DcfModel;
import jar.StockValueApp.repository.DcfModelRepository;
import jar.StockValueApp.repository.UserRepository;
import jar.StockValueApp.service.calculationService.MathService;
import jar.StockValueApp.service.mappingService.DcfModelMappingService;
import jar.StockValueApp.validator.DcfRequestValidator;
import jar.StockValueApp.validator.GlobalExceptionValidator;
import jar.StockValueApp.validator.UserRequestValidator;
import lombok.Data;
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
@Data
public class DcfModelService {

    private final DcfModelRepository dcfModelRepository;
    private final DcfModelMappingService dcfModelMappingService;
    private final GlobalExceptionValidator globalExceptionValidator;
    private final DcfRequestValidator dcfRequestValidator;
    private final UserRequestValidator userRequestValidator;
    private final UserRepository userRepository;
    private final CacheService cacheService;
    private final MathService mathService;

    @Cacheable(value = "dcfValuationsCache")
    public List<DcfModel> getAllDcfValuations() {
        final var dcfValuations = dcfModelRepository.findAll();
        dcfRequestValidator.validateDcfModelList(dcfValuations);

        log.info(dcfValuations.size() + " Grahams valuations were found in DB.");
        return dcfValuations;
    }

    @Cacheable(value = "dcfValuationsByTickerCache", key = "#ticker.concat('-').concat(#userId.toString())")
    public ArrayList<Object> getDcfValuationsByTicker(final String ticker, final Long userId) {
        globalExceptionValidator.validateId(userId);
        userRequestValidator.validateUserById(userId);
        final var companiesValuations = dcfModelRepository.findByUserId(userId);

        final var filteredCompaniesByTicker = companiesValuations.stream()
                .filter(valuation -> valuation.getTicker().equalsIgnoreCase(ticker))
                .collect(Collectors.toList());

        dcfRequestValidator.validateDcfModelList(filteredCompaniesByTicker, ticker);

        log.info("Found " + filteredCompaniesByTicker.size()
                + " Discounted cash flow company valuations with ticker: " + ticker);

        return dcfModelMappingService.mapToResponse(filteredCompaniesByTicker);
    }

    @Cacheable(value = "dcfValuationsByCompanyNameCache", key = "#companyName.concat('-').concat(#userId.toString())")
    public ArrayList<Object> getDcfValuationsByCompanyName(final String companyName, final Long userId) {
        globalExceptionValidator.validateId(userId);
        userRequestValidator.validateUserById(userId);
        final var companiesValuations = dcfModelRepository.findByUserId(userId);

        final var filteredCompaniesByCompanyName = companiesValuations.stream()
                .filter(valuation -> valuation.getCompanyName().equalsIgnoreCase(companyName))
                .collect(Collectors.toList());

        dcfRequestValidator.validateDcfModelList(filteredCompaniesByCompanyName, companyName);

        log.info("Found " + filteredCompaniesByCompanyName.size()
                + " Discounted cash flow company valuations with ticker: " + companyName);

        return dcfModelMappingService.mapToResponse(filteredCompaniesByCompanyName);
    }

    @Cacheable(
            value = "dcfValuationByDateRangeCache",
            key = "#startDate.toString().concat('-').concat(#endDate.toString()).concat('-').concat(#userId.toString())"
    )
    public ArrayList<Object> getDcfValuationByDate(
            final LocalDate startDate,
            final LocalDate endDate,
            final Long userId
    ){
        globalExceptionValidator.validateId(userId);
        userRequestValidator.validateUserById(userId);
        final var valuations = dcfModelRepository.findByUserId(userId);

        final var filteredValuationsByDate = valuations.stream()
                .filter(valuation ->
                        valuation.getCreationDate().isAfter(startDate.minusDays(1)) &&
                                valuation.getCreationDate().isBefore(endDate.plusDays(1)))
                .collect(Collectors.toList());

        dcfRequestValidator.validateDcfModelList(filteredValuationsByDate, startDate, endDate);

        log.info("Found " + filteredValuationsByDate.size()
                + " Discounted cash flow valuations between: " + startDate + " and " + endDate);
        return dcfModelMappingService.mapToResponse(filteredValuationsByDate);
    }

    public DcfModelResponseDTO addDcfValuation(final DcfModelRequestDTO dcfModelRequestDTO, final Long userId) {
        globalExceptionValidator.validateId(userId);
        userRequestValidator.validateUserById(userId);
        dcfRequestValidator.validateDcfModelRequest(dcfModelRequestDTO);
        final var user = userRepository.getReferenceById(userId);

        final var dcfModel = dcfModelMappingService.mapToEntity(dcfModelRequestDTO);
        user.getDcfModels().add(dcfModel);
        dcfModel.setUser(user);

        cacheService.evictAllDcfValuationsCaches();
        dcfModelRepository.save(dcfModel);
        log.info("Calculation created successfully.");
        return dcfModelMappingService.mapToResponse(dcfModel);
    }

    public HashMap<Object, Object> deleteDcfValuationById(final Long valuationId, final Long userId) {
        globalExceptionValidator.validateId(valuationId);
        globalExceptionValidator.validateId(userId);
        dcfRequestValidator.validateDcfModelById(valuationId);
        dcfRequestValidator.validateDcfModelForUser(valuationId, userId);

        cacheService.evictAllDcfValuationsCaches();
        dcfModelRepository.deleteById(valuationId);
        log.info("Discounted cash flow valuation  with id number " + valuationId + " was deleted from DB successfully.");

        return createDeletionResponse(valuationId);
    }

    private HashMap<Object, Object> createDeletionResponse(Long valuationId){
        var response = new HashMap<>();
        response.put("message", "Discounted cash flow valuation with id number " + valuationId + " was deleted from DB successfully.");
        return response;
    }
}
