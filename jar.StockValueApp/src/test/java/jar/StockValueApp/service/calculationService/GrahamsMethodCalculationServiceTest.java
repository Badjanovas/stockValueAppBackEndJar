package jar.StockValueApp.service.calculationService;

import jar.StockValueApp.dto.GrahamsRequestDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class GrahamsMethodCalculationServiceTest {

    @Autowired
    private GrahamsMethodCalculationService service;

    @Test
    void validateGrahamsCalculation_Returns_46_68(){
        final var requestDTO = new GrahamsRequestDTO(
                "test",
                "test",
                4.5,
                5.0,
                5.09
        );

        final var result = service.calculateGrahamsValuation(requestDTO);
        assertEquals(46.68, result);
    }

}