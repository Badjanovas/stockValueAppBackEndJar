package jar.StockValueApp.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "user_name")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements Serializable, UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String password;

    @Email
    @Column(nullable = false)
    private String email;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDate creationDate;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<GrahamsModel> grahamsModels;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<DcfModel> dcfModels;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<DividendDiscountModel> dividendDiscountModels;


    public User(String userName, String password, String email) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.grahamsModels = new ArrayList<>();
        this.dcfModels = new ArrayList<>();
        this.dividendDiscountModels = new ArrayList<>();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
