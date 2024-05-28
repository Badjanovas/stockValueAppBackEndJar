package jar.StockValueApp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class DividendDiscountRequestDTO  {

    private String companyName;
    private String companyTicker;
    private Double currentYearsDiv;
    private Double wacc;
    private Double expectedGrowthRate;

}
