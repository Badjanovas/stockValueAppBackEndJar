package jar.StockValueApp.exception;

public class MandatoryFieldsMissingException extends RuntimeException{
    public MandatoryFieldsMissingException(String message){
        super(message);
    }
}
