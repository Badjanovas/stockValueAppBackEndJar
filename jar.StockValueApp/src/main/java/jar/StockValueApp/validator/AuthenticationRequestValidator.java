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

    public void validateAuthenticationRequest(AuthenticationRequest request) throws MandatoryFieldsMissingException {
        if (request == null){
            log.error("Request was empty.");
            throw new MandatoryFieldsMissingException("Request was empty.");
        } else if (request.getUserName() == null ) {
            log.error("Mandatory user name field is missing.");
            throw new MandatoryFieldsMissingException("Mandatory user name field is missing.");
        } else if (request.getUserName().isBlank()) {
            log.error("Mandatory user name field is empty.");
            throw new MandatoryFieldsMissingException("Mandatory user name field is empty.");
        } else if (request.getPassword() == null) {
            log.error("Mandatory password field is missing.");
            throw new MandatoryFieldsMissingException("Mandatory password field is missing.");
        } else if (request.getPassword().isBlank()) {
            log.error("Mandatory password field is empty.");
            throw new MandatoryFieldsMissingException("Mandatory password field is empty.");
        }
    }

}
