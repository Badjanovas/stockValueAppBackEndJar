package jar.StockValueApp.service.calculationService;

import jar.StockValueApp.dto.DividendDiscountRequestDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class DividendDiscountCalculationServiceTest {

    @Autowired
    private DividendDiscountCalculationService service;

    @Test
    void validateCalculationOfDividendDiscountValue_Returns_63_6() {
        DividendDiscountRequestDTO requestDTO = new DividendDiscountRequestDTO(
                "test",
                "test",
                1.5,
                8.5,
                6.0
        );

        double result = service.calculateDividendDiscountValue(requestDTO);
        assertEquals(63.6, result);
    }

    @Test
    void validateCalculationOfNextYearsDiv() {
        DividendDiscountRequestDTO requestDTO = new DividendDiscountRequestDTO(
                "test",
                "test",
                1.5,
                8.5,
                6.0
        );
        double result = service.calculateValueOfNextYearsDiv(requestDTO);
        assertEquals(1.59, result);
    }
}