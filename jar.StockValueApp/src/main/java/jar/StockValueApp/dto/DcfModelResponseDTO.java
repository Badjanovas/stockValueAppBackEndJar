package jar.StockValueApp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DcfModelResponseDTO implements Serializable {

    private Long id;
    private String companyName;
    private String companyTicker;
    private Double sumOfFCF;
    private Double cashAndCashEquivalents;
    private Double totalDebt;
    private Double equityValue;
    private Double sharesOutstanding;
    private Double intrinsicValue;
    private LocalDate creationDate;

}
