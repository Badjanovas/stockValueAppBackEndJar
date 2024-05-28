package jar.StockValueApp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDTO {

    private Long id;
    private String userName;
    private String email;
    private LocalDate creationDate;

}
