package jar.StockValueApp.validator;


import jar.StockValueApp.exception.NotValidIdException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GlobalExceptionValidator {

    public void validateId(final Long id) {
        if (id == null || id <= 0) {
            log.error("Invalid id.");
            throw new NotValidIdException("Invalid id.");
        }
    }
}
