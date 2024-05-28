package jar.StockValueApp.service.calculationService;

import jar.StockValueApp.dto.GrahamsRequestDTO;
import jar.StockValueApp.model.GrahamsModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GrahamsMethodCalculationService {

    private final MathService mathService;

    public Double calculateGrahamsValuation(final GrahamsRequestDTO requestDTO) {
        return mathService.roundToTwoDecimal((requestDTO.getEps() *
                (GrahamsModel.BASE_PE + requestDTO.getGrowthRate()) *
                GrahamsModel.AVERAGE_YIELD_AAA_BONDS) / requestDTO.getCurrentYieldOfBonds());
    }
}
