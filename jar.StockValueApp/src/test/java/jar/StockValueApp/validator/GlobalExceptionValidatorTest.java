package jar.StockValueApp.validator;


import jar.StockValueApp.exception.NotValidIdException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
@SpringBootTest
class GlobalExceptionValidatorTest {

    @Autowired
    private GlobalExceptionValidator validator;

    /* Tests for validateId */
    @Test
    void validateId_IdNull_ThrowsException(){
        final var exception = assertThrows(
                NotValidIdException.class,
                () -> validator.validateId(null)
        );

        Assertions.assertEquals("Invalid id.", exception.getMessage());
    }

    @Test
    void validateId_IdNegativeValue_ThrowsException(){
        final var exception = assertThrows(
                NotValidIdException.class,
                () -> validator.validateId(-1L)
        );

        Assertions.assertEquals("Invalid id.", exception.getMessage());
    }

    @Test
    void validateId_ValidId_DoesNotThrowException(){
        assertDoesNotThrow(() -> validator.validateId(1L));
    }

}