package jar.StockValueApp.validator;


import jar.StockValueApp.exception.MandatoryFieldsMissingException;
import jar.StockValueApp.model.AuthenticationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationRequestValidator {

    public void validateAuthenticationRequest(AuthenticationRequest request) {
        if (request == null) {
            logAndThrow("Request was empty.");
        }

        checkMandatoryField(request.getUserName(), "user name");
        checkMandatoryField(request.getPassword(), "password");
    }

    private void checkMandatoryField(Object field, String fieldName) {
        if (field == null) {
            logAndThrow("Mandatory " + fieldName + " field is missing.");
        } else if (field instanceof String && ((String) field).isBlank()) {
            logAndThrow("Mandatory " + fieldName + " field is empty.");
        }
    }

    private void logAndThrow(String message) {
        log.error(message);
        throw new MandatoryFieldsMissingException(message);
    }

}
