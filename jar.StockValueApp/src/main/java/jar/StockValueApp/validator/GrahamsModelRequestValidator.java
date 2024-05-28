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

    public void validateGrahamsModelRequest(final GrahamsRequestDTO grahamsRequestDTO) throws MandatoryFieldsMissingException {
        if (grahamsRequestDTO == null){
            log.error("Request was empty.");
            throw new MandatoryFieldsMissingException("Request was empty.");
        } else if (grahamsRequestDTO.getCompanyName() == null){
            log.error("Mandatory company name field is missing.");
            throw new MandatoryFieldsMissingException("Mandatory company name field is missing.");
        } else if (grahamsRequestDTO.getCompanyName().isBlank()){
            log.error("Mandatory company name field is empty.");
            throw new MandatoryFieldsMissingException("Mandatory company name field is empty.");
        } else if (grahamsRequestDTO.getCompanyTicker() == null){
            log.error("Mandatory company ticker field is missing.");
            throw new MandatoryFieldsMissingException("Mandatory company ticker field is missing.");
        } else if (grahamsRequestDTO.getCompanyTicker().isBlank()){
            log.error("Mandatory company ticker field is empty.");
            throw new MandatoryFieldsMissingException("Mandatory company ticker field is empty.");
        } else if (grahamsRequestDTO.getEps() == null){
            log.error("Mandatory eps field is empty.");
            throw new MandatoryFieldsMissingException("Mandatory eps field is empty.");
        } else if (grahamsRequestDTO.getGrowthRate() == null){
            log.error("Mandatory growth rate field is empty.");
            throw new MandatoryFieldsMissingException("Mandatory growth rate field is empty.");
        } else if (grahamsRequestDTO.getCurrentYieldOfBonds() == null){
            log.error("Mandatory current yield of bonds field is empty.");
            throw new MandatoryFieldsMissingException("Mandatory current yield of bonds field is empty.");
        }
    }

    public void validateGrahamsModelById(final Long id) throws NoGrahamsModelFoundException {
        if (!grahamsModelRepository.existsById(id)){
            log.error("Grahams valuation with id number " + id + " not found.");
            throw new NoGrahamsModelFoundException("Grahams valuation with id number " + id + " not found.");
        }
    }

    public void validateGrahamsModelList(final List<GrahamsModel> grahamsValuations, String companyNameOrTicker)
            throws NoGrahamsModelFoundException {
        if (grahamsValuations.isEmpty()){
            log.error("No Graham valuations found for: " + companyNameOrTicker + ".");
            throw new NoGrahamsModelFoundException("No Graham valuations found for: " + companyNameOrTicker + ".");
        }
    }

    public void validateGrahamsModelList(final List<GrahamsModel> valuationList) throws NoGrahamsModelFoundException {
        if (valuationList.isEmpty()){
            log.error("No Graham valuations found.");
            throw new NoGrahamsModelFoundException("No Graham valuations found.");
        }
    }

    public void validateGrahamsModelList(final List<GrahamsModel> valuationList, LocalDate startDate, LocalDate endDate)
            throws NoGrahamsModelFoundException {
        if (valuationList.isEmpty()){
            log.error("There are no valuations made between " + startDate + " and " + endDate);
            throw new NoGrahamsModelFoundException("There are no valuations made between " + startDate + " and " + endDate);
        }
    }

    public void validateGrahamsModelForUser(final Long valuationId, final Long userId)
            throws ValuationDoestExistForSelectedUserException {
        Optional<GrahamsModel> valuation = grahamsModelRepository.findById(valuationId);
        if (valuation.isEmpty() || !valuation.get().getUser().getId().equals(userId)) {
            log.error("Valuation does not exist for this user");
            throw new ValuationDoestExistForSelectedUserException("Valuation does not exist for this user");
        }
    }
}
