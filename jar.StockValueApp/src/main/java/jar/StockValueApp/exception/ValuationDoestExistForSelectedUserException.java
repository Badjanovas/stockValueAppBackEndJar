package jar.StockValueApp.exception;

public class ValuationDoestExistForSelectedUserException extends RuntimeException{
    public ValuationDoestExistForSelectedUserException(String message) {
        super(message);
    }
}
