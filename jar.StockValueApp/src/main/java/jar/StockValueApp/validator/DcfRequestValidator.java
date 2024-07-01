package jar.StockValueApp.validator;


import jar.StockValueApp.dto.DcfModelRequestDTO;
import jar.StockValueApp.exception.MandatoryFieldsMissingException;
import jar.StockValueApp.exception.NoDcfValuationsFoundException;
import jar.StockValueApp.exception.NoGrahamsModelFoundException;
import jar.StockValueApp.exception.ValuationDoestExistForSelectedUserException;
import jar.StockValueApp.model.DcfModel;
import jar.StockValueApp.repository.DcfModelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DcfRequestValidator {

    private final DcfModelRepository dcfModelRepository;

    public void validateDcfModelRequest(final DcfModelRequestDTO dcfModelRequestDTO) {
        if (dcfModelRequestDTO == null) {
            logAndThrow("Request was empty.");
        }

        checkMandatoryField(dcfModelRequestDTO.getCompanyName(), "company name");
        checkMandatoryField(dcfModelRequestDTO.getCompanyTicker(), "company ticker");
        checkMandatoryField(dcfModelRequestDTO.getSumOfFCF(), "sumOfFCF");
        checkMandatoryField(dcfModelRequestDTO.getCashAndCashEquivalents(), "cashAndCashEquivalents");
        checkMandatoryField(dcfModelRequestDTO.getTotalDebt(), "totalDebt");
        checkMandatoryField(dcfModelRequestDTO.getSharesOutstanding(), "sharesOutstanding");
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

    public void validateDcfModelList(List<DcfModel> dcfValuations) {
        if (dcfValuations.isEmpty()) {
            log.error("No discounted cash flow valuations found.");
            throw new NoDcfValuationsFoundException("No discounted cash flow valuations found.");
        }
    }

    public void validateDcfModelList(List<DcfModel> dcfValuations, String tickerOrName) {
        if (dcfValuations.isEmpty()) {
            log.error("No discounted cash flow valuations found for: " + tickerOrName + ".");
            throw new NoDcfValuationsFoundException("No discounted cash flow valuations found for: " + tickerOrName + ".");
        }
    }

    public void validateDcfModelList(
            final List<DcfModel> valuationList,
            final LocalDate startDate,
            final LocalDate endDate
    ) {
        if (valuationList.isEmpty()) {
            log.error("There are no valuations made between " + startDate + " " + endDate);
            throw new NoGrahamsModelFoundException("There are no valuations made between " + startDate + " " + endDate);
        }
    }

    public void validateDcfModelById(final Long id) {
        if (!dcfModelRepository.existsById(id)) {
            log.error("Discounted cash flow valuation with id number " + id + " not found.");
            throw new NoDcfValuationsFoundException("Discounted cash flow valuation with id number " + id + " not found.");
        }
    }

    public void validateDcfModelForUser(final Long valuationId, final Long userId) {
        final var valuation = dcfModelRepository.findById(valuationId);
        if (valuation.isEmpty() || !valuation.get().getUser().getId().equals(userId)) {
            log.error("Valuation does not exist for this user");
            throw new ValuationDoestExistForSelectedUserException("Valuation does not exist for this user");
        }
    }
}
