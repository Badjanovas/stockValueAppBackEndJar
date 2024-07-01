package jar.StockValueApp.exception;

public class NoDcfValuationsFoundException extends RuntimeException{
    public NoDcfValuationsFoundException(String message) {
        super(message);
    }
}
