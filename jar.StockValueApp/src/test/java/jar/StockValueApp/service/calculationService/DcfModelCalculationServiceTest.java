package jar.StockValueApp.service.calculationService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class DcfModelCalculationServiceTest {

    @Autowired
    private DcfModelCalculationService service;

    @Test
    void validateCalculationOfSumOfDiscountedFCF_Returns_21000(){
        double sumOfFCF = 5000.0;
        double wacc = 0.1;
        double growthRate = 0.05;

        double result  = service.calculateSumOfDiscountedFCF(sumOfFCF,wacc,growthRate);
        assertEquals(21000.0, result);
    }

}