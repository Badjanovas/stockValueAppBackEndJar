package jar.StockValueApp.service.calculationService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class MathServiceTest {

    @Autowired
    private MathService mathService;

    @Test
    void validateConvertToPercentages_Returns_1(){
       double result = mathService.convertToPercentages(100);
       assertEquals(1.0, result);
    }

    @Test
    void validateRoundToTwoDecimals_ReturnsNumberWithTwoDecimals(){
        double result = mathService.roundToTwoDecimal(5.5555);
        assertEquals(5.56, result);
    }



}