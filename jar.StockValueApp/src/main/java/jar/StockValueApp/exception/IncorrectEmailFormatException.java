package jar.StockValueApp.exception;

public class IncorrectEmailFormatException extends RuntimeException{
    public IncorrectEmailFormatException(String message) {
        super(message);
    }
}
