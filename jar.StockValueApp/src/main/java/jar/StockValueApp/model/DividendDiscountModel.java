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
public class DividendDiscountModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String ticker;

    @Column(nullable = false)
    private Double currentYearsDiv;

    @Column(nullable = false)
    private Double valueOfNextYearsDiv;

    @Column(nullable = false)
    //Weighted average cost of capital
    private Double wacc;

    @Column(nullable = false)
    private Double expectedGrowthRate;

    @Column(nullable = false)
    private Double intrinsicValue;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDate creationDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;
}
