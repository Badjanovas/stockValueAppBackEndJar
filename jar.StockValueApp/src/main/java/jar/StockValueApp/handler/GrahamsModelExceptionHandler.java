package jar.StockValueApp.handler;


import jar.StockValueApp.exception.NoGrahamsModelFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.format.DateTimeParseException;

@ControllerAdvice
@Slf4j
public class GrahamsModelExceptionHandler {

    @ExceptionHandler(NoGrahamsModelFoundException.class)
    public ResponseEntity<Object> handleNoGrahamsModelFoundException(NoGrahamsModelFoundException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<Object> handleDateTimeParseException(DateTimeParseException e) {
        log.error("Date format is invalid. Please use YYYY-MM-DD format.");
        return new ResponseEntity<>("Date format is invalid. Please use YYYY-MM-DD format.", HttpStatus.BAD_REQUEST);
    }


}
