package jar.StockValueApp.handler;


import jar.StockValueApp.exception.NoDcfValuationsFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DcfExceptionHandler {

    @ExceptionHandler(NoDcfValuationsFoundException.class)
    public ResponseEntity<Object> handleNoDcfValuationsFoundException(NoDcfValuationsFoundException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

}
