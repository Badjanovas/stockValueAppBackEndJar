package jar.StockValueApp.validator;


import jar.StockValueApp.dto.DividendDiscountRequestDTO;
import jar.StockValueApp.exception.IncorrectCompaniesExpectedGrowthException;
import jar.StockValueApp.exception.MandatoryFieldsMissingException;
import jar.StockValueApp.exception.NoDividendDiscountModelFoundException;
import jar.StockValueApp.exception.ValuationDoestExistForSelectedUserException;
import jar.StockValueApp.model.DividendDiscountModel;
import jar.StockValueApp.repository.DividendDiscountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class DividendDiscountRequestValidator {

    private final DividendDiscountRepository dividendDiscountRepository;

    public void validateDividendDiscountRequest(final DividendDiscountRequestDTO dividendDiscountRequestDTO) {
        if (dividendDiscountRequestDTO == null) {
            logAndThrow("Request was empty.");
        }

        checkMandatoryField(dividendDiscountRequestDTO.getCompanyName(), "company name");
        checkMandatoryField(dividendDiscountRequestDTO.getCompanyTicker(), "company ticker");
        checkMandatoryField(dividendDiscountRequestDTO.getCurrentYearsDiv(), "currentYearsDiv");
        checkMandatoryField(dividendDiscountRequestDTO.getWacc(), "wacc");
        checkMandatoryField(dividendDiscountRequestDTO.getExpectedGrowthRate(), "expectedGrowthRate");
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

    public void validateDividendDiscountList(final List<DividendDiscountModel> dividendDiscountModels) {
        if (dividendDiscountModels.isEmpty()) {
            log.error("No dividend discount valuations found.");
            throw new NoDividendDiscountModelFoundException("No dividend discount valuations found.");
        }
    }

    public void validateDividendDiscountList(
            final List<DividendDiscountModel> dividendDiscountModels,
            final String companyNameOrTicker
    ) {
        if (dividendDiscountModels.isEmpty()) {
            log.error("No dividend discount valuations found for: " + companyNameOrTicker + ".");
            throw new NoDividendDiscountModelFoundException("No dividend discount valuations found for: "
                    + companyNameOrTicker + ".");
        }
    }

    public void validateDividendDiscountList(
            final List<DividendDiscountModel> dividendDiscountModels,
            final LocalDate startDate,
            final LocalDate endDate
    ) {
        if (dividendDiscountModels.isEmpty()) {
            log.error("There are no valuations made between " + startDate + " and " + endDate);
            throw new NoDividendDiscountModelFoundException("There are no valuations made between " + startDate + " end " + endDate);
        }
    }

    public void validateDividendDiscountById(final Long id) {
        if (!dividendDiscountRepository.existsById(id)) {
            log.error("Dividend discount valuation with id number " + id + " not found.");
            throw new NoDividendDiscountModelFoundException("Dividend discount valuation with id number "
                    + id + " not found.");
        }
    }

    public void validateExpectedGrowthRateInput(final Double wacc, final Double expectedGrowthRate) {
        if (expectedGrowthRate >= wacc) {
            log.error("Dividend discount model isn't suitable to calculate intrinsic value if expected " +
                    "growth rate is higher or equal to the weighted average cost of capital.");
            throw new IncorrectCompaniesExpectedGrowthException("Dividend discount model isn't suitable to calculate" +
                    " intrinsic value if expected growth rate is higher or equal to the weighted average cost of capital.");
        }
    }

    public void validateDividendDiscountModelForUser(final Long valuationId, final Long userId) {
        final var valuation = dividendDiscountRepository.findById(valuationId);
        if (valuation.isEmpty() || !valuation.get().getUser().getId().equals(userId)) {
            log.error("Valuation does not exist for this user");
            throw new ValuationDoestExistForSelectedUserException("Valuation does not exist for this user");
        }
    }

}
