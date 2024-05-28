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
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class DcfRequestValidator {

    private final DcfModelRepository dcfModelRepository;

    public void validateDcfModelRequest(final DcfModelRequestDTO dcfModelRequestDTO) throws MandatoryFieldsMissingException {
        if (dcfModelRequestDTO == null) {
            log.error("Request was empty.");
            throw new MandatoryFieldsMissingException("Request was empty.");
        } else if (dcfModelRequestDTO.getCompanyName() == null) {
            log.error("Mandatory company name field is missing.");
            throw new MandatoryFieldsMissingException("Mandatory company name field is missing.");
        } else if (dcfModelRequestDTO.getCompanyName().isBlank()) {
            log.error("Mandatory company name field is empty.");
            throw new MandatoryFieldsMissingException("Mandatory company name field is empty.");
        } else if (dcfModelRequestDTO.getCompanyTicker() == null) {
            log.error("Mandatory company ticker field is missing.");
            throw new MandatoryFieldsMissingException("Mandatory company ticker field is missing.");
        } else if (dcfModelRequestDTO.getCompanyTicker().isBlank()) {
            log.error("Mandatory company ticker field is empty.");
            throw new MandatoryFieldsMissingException("Mandatory company ticker field is empty.");
        } else if (dcfModelRequestDTO.getSumOfFCF() == null) {
            log.error("Mandatory sumOfFCF field is missing.");
            throw new MandatoryFieldsMissingException("Mandatory sumOfFCF field is missing.");
        } else if (dcfModelRequestDTO.getCashAndCashEquivalents() == null) {
            log.error("Mandatory cashAndCashEquivalents field is missing.");
            throw new MandatoryFieldsMissingException("Mandatory cashAndCashEquivalents field is missing.");
        } else if (dcfModelRequestDTO.getTotalDebt() == null) {
            log.error("Mandatory totalDebt field is missing.");
            throw new MandatoryFieldsMissingException("Mandatory totalDebt field is missing.");
        } else if (dcfModelRequestDTO.getSharesOutstanding() == null) {
            log.error("Mandatory sharesOutstanding field is missing.");
            throw new MandatoryFieldsMissingException("Mandatory sharesOutstanding field is missing.");
        }
    }

    public void validateDcfModelList(List<DcfModel> dcfValuations) throws NoDcfValuationsFoundException {
        if (dcfValuations.isEmpty()){
            log.error("No discounted cash flow valuations found.");
            throw new NoDcfValuationsFoundException("No discounted cash flow valuations found.");
        }
    }

    public void validateDcfModelList(List<DcfModel> dcfValuations, String tickerOrName)
            throws NoDcfValuationsFoundException {
        if (dcfValuations.isEmpty()){
            log.error("No discounted cash flow valuations found for: " + tickerOrName + ".");
            throw new NoDcfValuationsFoundException("No discounted cash flow valuations found for: " + tickerOrName + ".");
        }
    }

    public void validateDcfModelList(
            final List<DcfModel> valuationList,
            final LocalDate startDate,
            final LocalDate endDate
    )
            throws NoGrahamsModelFoundException {
        if (valuationList.isEmpty()){
            log.error("There are no valuations made between " + startDate + " " + endDate);
            throw new NoGrahamsModelFoundException("There are no valuations made between " + startDate + " " + endDate);
        }
    }

    public void validateDcfModelById(final Long id) throws NoDcfValuationsFoundException {
        if (!dcfModelRepository.existsById(id)){
            log.error("Discounted cash flow valuation with id number " + id + " not found.");
            throw new NoDcfValuationsFoundException("Discounted cash flow valuation with id number " + id + " not found.");
        }
    }

    public void validateDcfModelForUser(final Long valuationId, final Long userId)
            throws ValuationDoestExistForSelectedUserException {
        Optional<DcfModel> valuation = dcfModelRepository.findById(valuationId);
        if (valuation.isEmpty() || !valuation.get().getUser().getId().equals(userId)){
            log.error("Valuation does not exist for this user");
            throw new ValuationDoestExistForSelectedUserException("Valuation does not exist for this user");
        }
    }

}
