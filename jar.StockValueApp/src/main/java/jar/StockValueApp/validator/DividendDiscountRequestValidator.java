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

    public void validateDividendDiscountRequest(final DividendDiscountRequestDTO dividendDiscountRequestDTO)
            throws MandatoryFieldsMissingException {
        if (dividendDiscountRequestDTO == null){
            log.error("Request was empty.");
            throw new MandatoryFieldsMissingException("Request was empty.");
        } else if (dividendDiscountRequestDTO.getCompanyName() == null) {
            log.error("Mandatory company name field is missing.");
            throw new MandatoryFieldsMissingException("Mandatory company name field is missing.");
        } else if (dividendDiscountRequestDTO.getCompanyName().isEmpty()) {
            log.error("Mandatory company name field is empty.");
            throw new MandatoryFieldsMissingException("Mandatory company name field is empty.");
        } else if (dividendDiscountRequestDTO.getCompanyTicker() == null ) {
            log.error("Mandatory company ticker field is missing.");
            throw new MandatoryFieldsMissingException("Mandatory company ticker field is missing.");
        } else if (dividendDiscountRequestDTO.getCompanyTicker().isBlank()) {
            log.error("Mandatory company ticker field is empty.");
            throw new MandatoryFieldsMissingException("Mandatory company ticker field is empty.");
        } else if (dividendDiscountRequestDTO.getCurrentYearsDiv() == null) {
            log.error("Mandatory currentYearsDiv field is missing.");
            throw new MandatoryFieldsMissingException("Mandatory currentYearsDiv field is missing.");
        } else if (dividendDiscountRequestDTO.getWacc() == null) {
            log.error("Mandatory wacc field is missing.");
            throw new MandatoryFieldsMissingException("Mandatory wacc field is missing.");
        } else if (dividendDiscountRequestDTO.getExpectedGrowthRate() == null) {
            log.error("Mandatory expectedGrowthRate field is missing.");
            throw new MandatoryFieldsMissingException("Mandatory expectedGrowthRate field is missing.");
        }
    }

    public void validateDividendDiscountList(final List<DividendDiscountModel> dividendDiscountModels)
            throws NoDividendDiscountModelFoundException {
        if (dividendDiscountModels.isEmpty()){
            log.error("No dividend discount valuations found.");
            throw new NoDividendDiscountModelFoundException("No dividend discount valuations found.");
        }
    }

    public void validateDividendDiscountList(
            final List<DividendDiscountModel> dividendDiscountModels,
            final String companyNameOrTicker
    )
            throws NoDividendDiscountModelFoundException
    {
        if (dividendDiscountModels.isEmpty()){
            log.error("No dividend discount valuations found for: " + companyNameOrTicker + ".");
            throw new NoDividendDiscountModelFoundException("No dividend discount valuations found for: "
                    + companyNameOrTicker + ".");
        }
    }

    public void validateDividendDiscountList(
            final List<DividendDiscountModel> dividendDiscountModels,
            final LocalDate startDate,
            final LocalDate endDate
    )
            throws NoDividendDiscountModelFoundException
    {
        if (dividendDiscountModels.isEmpty()){
            log.error("There are no valuations made between " + startDate + " and " + endDate);
            throw new NoDividendDiscountModelFoundException("There are no valuations made between " + startDate + " end " + endDate);
        }
    }

    public void validateDividendDiscountById(final Long id) throws NoDividendDiscountModelFoundException {
        if (!dividendDiscountRepository.existsById(id)){
            log.error("Dividend discount valuation with id number " + id + " not found.");
            throw new NoDividendDiscountModelFoundException("Dividend discount valuation with id number "
                    + id + " not found.");
        }
    }

    public void validateExpectedGrowthRateInput(final Double wacc, final Double expectedGrowthRate)
            throws IncorrectCompaniesExpectedGrowthException {
        if (expectedGrowthRate >= wacc){
            log.error("Dividend discount model isn't suitable to calculate intrinsic value if expected " +
                    "growth rate is higher or equal to the weighted average cost of capital.");
            throw new IncorrectCompaniesExpectedGrowthException("Dividend discount model isn't suitable to calculate" +
                    " intrinsic value if expected growth rate is higher or equal to the weighted average cost of capital.");
        }
    }

    public void validateDividendDiscountModelForUser(final Long valuationId, final Long userId)
            throws ValuationDoestExistForSelectedUserException {
        Optional<DividendDiscountModel> valuation = dividendDiscountRepository.findById(valuationId);
        if (valuation.isEmpty() || !valuation.get().getUser().getId().equals(userId)){
            log.error("Valuation does not exist for this user");
            throw new ValuationDoestExistForSelectedUserException("Valuation does not exist for this user");
        }
    }

}
