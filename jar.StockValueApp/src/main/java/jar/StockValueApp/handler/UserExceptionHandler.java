package jar.StockValueApp.handler;


import jar.StockValueApp.exception.EmailAlreadyExistException;
import jar.StockValueApp.exception.IncorrectEmailFormatException;
import jar.StockValueApp.exception.NoUsersFoundException;
import jar.StockValueApp.exception.UserAlreadyExistException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class UserExceptionHandler {

    @ExceptionHandler(NoUsersFoundException.class)
    public ResponseEntity<Object> handleNoUsersFoundException(NoUsersFoundException e) {
        Map<String, String> response = new HashMap<>();
        response.put("message", e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // The method handleUserAlreadyExistException captures a UserAlreadyExistException,
    // packages its message into a JSON object, and returns it with an HTTP 409 Conflict status,
    // indicating that the user already exists.
    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<Object> handleUserAlreadyExistException(UserAlreadyExistException e) {
        Map<String, String> response = new HashMap<>();
        response.put("message", e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IncorrectEmailFormatException.class)
    public ResponseEntity<Object> incorrectEmailFormatException(IncorrectEmailFormatException e) {
        Map<String, String> response = new HashMap<>();
        response.put("message", e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<Object> emailAlreadyExist(EmailAlreadyExistException e) {
        Map<String, String> response = new HashMap<>();
        response.put("message", e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
}
