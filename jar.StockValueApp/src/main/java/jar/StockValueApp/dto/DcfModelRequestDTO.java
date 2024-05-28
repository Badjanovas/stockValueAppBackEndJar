package jar.StockValueApp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class DcfModelRequestDTO {

    private String companyName;
    private String companyTicker;
    private Double sumOfFCF;
    private Double cashAndCashEquivalents;
    private Double totalDebt;
    private Double sharesOutstanding;
    private Double wacc;
    private Double growthRate;

}
