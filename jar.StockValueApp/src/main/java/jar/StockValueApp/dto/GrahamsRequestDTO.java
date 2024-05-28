package jar.StockValueApp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class GrahamsRequestDTO {

    private String companyName;
    private String companyTicker;
    private Double eps;
    private Double growthRate;
    private Double currentYieldOfBonds;

}
