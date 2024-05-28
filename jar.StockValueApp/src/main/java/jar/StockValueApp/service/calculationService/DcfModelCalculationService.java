package jar.StockValueApp.service.calculationService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DcfModelCalculationService {

    private final MathService mathService;

    public double calculateSumOfDiscountedFCF(double sumOfFCF, double wacc, double growthRate) {
        double initialFCF = sumOfFCF / 5;
        double sumOfDiscountedFCF = calculateDiscountedFCF(initialFCF, wacc, growthRate);
        double discountedTerminalValue = calculateDiscountedTerminalValue(initialFCF, wacc, growthRate);

        return sumOfDiscountedFCF + discountedTerminalValue;
    }

    private double calculateDiscountedFCF(double initialFCF, double wacc, double growthRate) {
        double sumOfDiscountedFCF = 0.0;

        for (int i = 1; i <= 5; i++) {
            double fcf = initialFCF * Math.pow(1 + growthRate, i);
            double discountFactor = Math.pow(1 + wacc, i);
            sumOfDiscountedFCF += fcf / discountFactor;
        }

        return sumOfDiscountedFCF;
    }

    private double calculateTerminalValue(double initialFCF, double growthRate, double wacc) {
        return initialFCF * Math.pow(1 + growthRate, 5) * (1 + growthRate) / (wacc - growthRate);
    }

    private double calculateDiscountedTerminalValue(double initialFCF, double wacc, double growthRate) {
        double terminalValue = calculateTerminalValue(initialFCF, growthRate, wacc);
        double discountFactor = Math.pow(1 + wacc, 5);
        return terminalValue / discountFactor;
    }

    public double calculateDcfValuation(double equityValue, double sharesOutstanding) {
        return mathService.roundToTwoDecimal(equityValue / sharesOutstanding);
    }

}
