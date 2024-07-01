package jar.StockValueApp.service.mappingService;


import jar.StockValueApp.dto.DcfModelRequestDTO;
import jar.StockValueApp.dto.DcfModelResponseDTO;
import jar.StockValueApp.model.DcfModel;
import jar.StockValueApp.service.calculationService.DcfModelCalculationService;
import jar.StockValueApp.service.calculationService.MathService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DcfModelMappingService {

    private final DcfModelCalculationService dcfCalculationService;
    private final MathService mathService;

    public DcfModel mapToEntity(final DcfModelRequestDTO requestDTO) {
        var wacc = mathService.convertToPercentages(requestDTO.getWacc());
        var growthRate = mathService.convertToPercentages(requestDTO.getGrowthRate());

        var sumOfDiscountedFCF = dcfCalculationService.calculateSumOfDiscountedFCF(requestDTO.getSumOfFCF(), wacc, growthRate);
        var equityValue = dcfCalculationService.calculateEquityValue(sumOfDiscountedFCF, requestDTO.getCashAndCashEquivalents(), requestDTO.getTotalDebt());
        var intrinsicValue = dcfCalculationService.calculateDcfPerShareValue(requestDTO.getSumOfFCF(), wacc, growthRate, requestDTO.getCashAndCashEquivalents(), requestDTO.getTotalDebt(), requestDTO.getSharesOutstanding());

        return DcfModel.builder()
                .companyName(requestDTO.getCompanyName())
                .ticker(requestDTO.getCompanyTicker())
                .sumOfFCF(requestDTO.getSumOfFCF())
                .cashAndCashEquivalents(requestDTO.getCashAndCashEquivalents())
                .totalDebt(requestDTO.getTotalDebt())
                .equityValue(mathService.roundToTwoDecimal(equityValue))
                .sharesOutstanding(requestDTO.getSharesOutstanding())
                .intrinsicValue(intrinsicValue)
                .build();
    }

    public ArrayList<Object> mapToResponse(final List<DcfModel> dcfValuations) {
        final var mappedDcfValuations = new ArrayList<>();
        for (DcfModel dcfValuation : dcfValuations) {
            DcfModelResponseDTO dto = DcfModelResponseDTO.builder()
                    .id(dcfValuation.getId())
                    .companyName(dcfValuation.getCompanyName())
                    .companyTicker(dcfValuation.getTicker())
                    .sumOfFCF(dcfValuation.getSumOfFCF())
                    .cashAndCashEquivalents(dcfValuation.getCashAndCashEquivalents())
                    .totalDebt(dcfValuation.getTotalDebt())
                    .equityValue(dcfValuation.getEquityValue())
                    .sharesOutstanding(dcfValuation.getSharesOutstanding())
                    .intrinsicValue(dcfValuation.getIntrinsicValue())
                    .creationDate(dcfValuation.getCreationDate())
                    .build();

            mappedDcfValuations.add(dto);
        }
        return mappedDcfValuations;
    }

    public DcfModelResponseDTO mapToResponse(final DcfModel dcfValuation){
        return DcfModelResponseDTO.builder()
                .id(dcfValuation.getId())
                .companyName(dcfValuation.getCompanyName())
                .companyTicker(dcfValuation.getTicker())
                .sumOfFCF(dcfValuation.getSumOfFCF())
                .cashAndCashEquivalents(dcfValuation.getCashAndCashEquivalents())
                .totalDebt(dcfValuation.getTotalDebt())
                .equityValue(dcfValuation.getEquityValue())
                .sharesOutstanding(dcfValuation.getSharesOutstanding())
                .intrinsicValue(dcfValuation.getIntrinsicValue())
                .creationDate(dcfValuation.getCreationDate())
                .build();
    }

}
