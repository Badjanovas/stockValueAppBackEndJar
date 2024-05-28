package jar.StockValueApp.handler;


import jar.StockValueApp.exception.MandatoryFieldsMissingException;
import jar.StockValueApp.exception.NotValidIdException;
import jar.StockValueApp.exception.ValuationDoestExistForSelectedUserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.ConnectException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(NotValidIdException.class)
    public ResponseEntity<Object> handleNotValidIdException(NotValidIdException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MandatoryFieldsMissingException.class)
    public ResponseEntity<Object> handleMandatoryFieldException(MandatoryFieldsMissingException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConnectException.class)
    public ResponseEntity<Object> handleConnectException(){
        log.error("Failed to connect to the database.");
        return new ResponseEntity<>("Failed to connect to the database.", HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<Object> handleNumberFormatException(){
        log.error("The provided input is not a valid number.");
        return new ResponseEntity<>("The provided input is not a valid number.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValuationDoestExistForSelectedUserException.class)
    public ResponseEntity<Object> handleValuationDoestExistForSelectedUser(ValuationDoestExistForSelectedUserException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}
