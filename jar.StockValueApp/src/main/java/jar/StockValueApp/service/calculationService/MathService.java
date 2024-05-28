package jar.StockValueApp.service.calculationService;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class MathService {

    public double convertToPercentages(double number) {
        return number / 100;
    }

    public double roundToTwoDecimal(double numberToRound) {
        return BigDecimal.valueOf(numberToRound)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

}
