package jar.StockValueApp.validator;

import jar.StockValueApp.dto.GrahamsRequestDTO;
import jar.StockValueApp.exception.MandatoryFieldsMissingException;
import jar.StockValueApp.exception.NoGrahamsModelFoundException;
import jar.StockValueApp.exception.ValuationDoestExistForSelectedUserException;
import jar.StockValueApp.model.GrahamsModel;
import jar.StockValueApp.repository.GrahamsModelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class GrahamsModelRequestValidator {

    private final GrahamsModelRepository grahamsModelRepository;

    public void validateGrahamsModelRequest(final GrahamsRequestDTO grahamsRequestDTO) {
        if (grahamsRequestDTO == null) {
            logAndThrow("Request was empty.");
        }

        checkMandatoryField(grahamsRequestDTO.getCompanyName(), "company name");
        checkMandatoryField(grahamsRequestDTO.getCompanyTicker(), "company ticker");
        checkMandatoryField(grahamsRequestDTO.getEps(), "eps");
        checkMandatoryField(grahamsRequestDTO.getGrowthRate(), "growth rate");
        checkMandatoryField(grahamsRequestDTO.getCurrentYieldOfBonds(), "current yield of bonds");
    }

    private void checkMandatoryField(Object field, String fieldName) {
        if (field == null) {
            logAndThrow("Mandatory " + fieldName + " field is missing.");
        } else if (field instanceof String && ((String) field).isBlank()) {
            logAndThrow("Mandatory " + fieldName + " field is empty.");
        }
    }

    private void logAndThrow(String message) {
        log.error(message);
        throw new MandatoryFieldsMissingException(message);
    }

    public void validateGrahamsModelById(final Long id) {
        if (!grahamsModelRepository.existsById(id)) {
            log.error("Grahams valuation with id number " + id + " not found.");
            throw new NoGrahamsModelFoundException("Grahams valuation with id number " + id + " not found.");
        }
    }

    public void validateGrahamsModelList(final List<GrahamsModel> grahamsValuations, String companyNameOrTicker) {
        if (grahamsValuations.isEmpty()) {
            log.error("No Graham valuations found for: " + companyNameOrTicker + ".");
            throw new NoGrahamsModelFoundException("No Graham valuations found for: " + companyNameOrTicker + ".");
        }
    }

    public void validateGrahamsModelList(final List<GrahamsModel> valuationList) {
        if (valuationList.isEmpty()) {
            log.error("No Graham valuations found.");
            throw new NoGrahamsModelFoundException("No Graham valuations found.");
        }
    }

    public void validateGrahamsModelList(final List<GrahamsModel> valuationList, LocalDate startDate, LocalDate endDate) {
        if (valuationList.isEmpty()) {
            log.error("There are no valuations made between " + startDate + " and " + endDate);
            throw new NoGrahamsModelFoundException("There are no valuations made between " + startDate + " and " + endDate);
        }
    }

    public void validateGrahamsModelForUser(final Long valuationId, final Long userId) {
        final var valuation = grahamsModelRepository.findById(valuationId);
        if (valuation.isEmpty() || !valuation.get().getUser().getId().equals(userId)) {
            log.error("Valuation does not exist for this user");
            throw new ValuationDoestExistForSelectedUserException("Valuation does not exist for this user");
        }
    }
}
