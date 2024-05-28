package jar.StockValueApp.exception;

public class MandatoryFieldsMissingException extends Exception{
    public MandatoryFieldsMissingException(String message){
        super(message);
    }
}
