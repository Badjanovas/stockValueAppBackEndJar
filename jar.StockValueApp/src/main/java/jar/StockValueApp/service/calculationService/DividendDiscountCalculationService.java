package jar.StockValueApp.service.calculationService;

import jar.StockValueApp.dto.DividendDiscountRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DividendDiscountCalculationService {

    private final MathService mathService;

    public Double calculateDividendDiscountValue(DividendDiscountRequestDTO requestDTO) {
        return mathService.roundToTwoDecimal(calculateValueOfNextYearsDiv(requestDTO)
                / (mathService.convertToPercentages(requestDTO.getWacc()) - mathService.convertToPercentages(requestDTO.getExpectedGrowthRate())));
    }
    public Double calculateValueOfNextYearsDiv(DividendDiscountRequestDTO requestDTO) {
        return mathService.roundToTwoDecimal(requestDTO.getCurrentYearsDiv() * (1 + mathService.convertToPercentages(requestDTO.getExpectedGrowthRate())));
    }
}
