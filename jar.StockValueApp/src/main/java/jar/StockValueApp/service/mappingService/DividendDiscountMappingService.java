package jar.StockValueApp.service.mappingService;


import jar.StockValueApp.dto.DividendDiscountRequestDTO;
import jar.StockValueApp.dto.DividendDiscountResponseDTO;
import jar.StockValueApp.model.DividendDiscountModel;
import jar.StockValueApp.service.calculationService.DividendDiscountCalculationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DividendDiscountMappingService {

    private final DividendDiscountCalculationService service;

//    Skaiciavimai sulusta jeigu paduodu vienoda skaiciu i wacc ir i expected
//    growth rate arba expected growth rate yra didesnis uz wacc
    public DividendDiscountModel mapToEntity(final DividendDiscountRequestDTO requestDTO) {
        return DividendDiscountModel.builder()
                .companyName(requestDTO.getCompanyName())
                .ticker(requestDTO.getCompanyTicker())
                .currentYearsDiv(requestDTO.getCurrentYearsDiv())
                .valueOfNextYearsDiv(service.calculateValueOfNextYearsDiv(requestDTO))
                .wacc(requestDTO.getWacc())
                .expectedGrowthRate(requestDTO.getExpectedGrowthRate())
                .intrinsicValue(service.calculateDividendDiscountValue(requestDTO))
                .build();
    }

    public List<DividendDiscountResponseDTO> mapToResponse(List<DividendDiscountModel> dividendDiscountValuations) {
        final List<DividendDiscountResponseDTO> mappedDividendValuations = new ArrayList<>();
        for (DividendDiscountModel valuation : dividendDiscountValuations) {
            DividendDiscountResponseDTO dto = DividendDiscountResponseDTO.builder()
                    .id(valuation.getId())
                    .companyName(valuation.getCompanyName())
                    .companyTicker(valuation.getTicker())
                    .currentYearsDiv(valuation.getCurrentYearsDiv())
                    .valueOfNextYearsDiv(valuation.getValueOfNextYearsDiv())
                    .wacc(valuation.getWacc())
                    .expectedGrowthRate(valuation.getExpectedGrowthRate())
                    .intrinsicValue(valuation.getIntrinsicValue())
                    .creationDate(valuation.getCreationDate())
                    .build();

            mappedDividendValuations.add(dto);
        }
        return mappedDividendValuations;
    }

    public DividendDiscountResponseDTO mapToResponse(final DividendDiscountModel dividendDiscountValuation){
        return DividendDiscountResponseDTO.builder()
                .id(dividendDiscountValuation.getId())
                .companyName(dividendDiscountValuation.getCompanyName())
                .companyTicker(dividendDiscountValuation.getTicker())
                .currentYearsDiv(dividendDiscountValuation.getCurrentYearsDiv())
                .valueOfNextYearsDiv(dividendDiscountValuation.getValueOfNextYearsDiv())
                .wacc(dividendDiscountValuation.getWacc())
                .expectedGrowthRate(dividendDiscountValuation.getExpectedGrowthRate())
                .intrinsicValue(dividendDiscountValuation.getIntrinsicValue())
                .creationDate(dividendDiscountValuation.getCreationDate())
                .build();
    }

}
