package jar.StockValueApp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DcfModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String ticker;

    @Column(nullable = false)
    private Double sumOfFCF;

    @Column(nullable = false)
    private Double cashAndCashEquivalents;

    @Column(nullable = false)
    private Double totalDebt;

    @Column(nullable = false)
    private Double equityValue;

    @Column(nullable = false)
    private Double sharesOutstanding;

    @Column(nullable = false)
    private Double intrinsicValue;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDate creationDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    public DcfModel(String companyName, String ticker, Double sumOfFCF, Double cashAndCashEquivalents, Double totalDebt, Double sharesOutstanding, User user) {
        this.companyName = companyName;
        this.ticker = ticker;
        this.sumOfFCF = sumOfFCF;
        this.cashAndCashEquivalents = cashAndCashEquivalents;
        this.totalDebt = totalDebt;
        this.sharesOutstanding = sharesOutstanding;
        this.user = user;
    }
}
