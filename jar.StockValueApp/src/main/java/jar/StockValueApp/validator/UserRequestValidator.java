package jar.StockValueApp.validator;

import jar.StockValueApp.dto.UserRequestDTO;
import jar.StockValueApp.exception.*;
import jar.StockValueApp.model.User;
import jar.StockValueApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserRequestValidator {

    private final UserRepository userRepository;

    public void validateUserRequest(final UserRequestDTO userRequestDTO) {
        if (userRequestDTO == null) {
            logAndThrow("Request was empty.");
        }

        checkMandatoryField(userRequestDTO.getUserName(), "user name");
        checkMandatoryField(userRequestDTO.getPassword(), "password");
        checkMandatoryField(userRequestDTO.getEmail(), "email");
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

    public void validateUserList(final List<User> users) {
        if (users.isEmpty()) {
            log.error("No users were found in the DB!");
            throw new NoUsersFoundException("No users were found!");
        }
    }

    public void validateUserById(final Long id) {
        if (!userRepository.existsById(id)) {
            log.error("User with id number " + id + " not found.");
            throw new NoUsersFoundException("User with id number " + id + " not found.");
        }
    }

    // Checks if the provided users username already exists in the system.
    public void validateUserName(final UserRequestDTO userRequestDTO) {
        final var user = userRepository.findByUserName(userRequestDTO.getUserName());

        if (user.isPresent()) {
            log.error("User with username: " + userRequestDTO.getUserName() +
                    " already exist. Choose different user name.");
            throw new UserAlreadyExistException("User with username: " + userRequestDTO.getUserName() +
                    " already exist. Choose different user name.");
        }
    }

    public void validateEmailFormat(final String email) {
        final var emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        final var emailPattern = Pattern.compile(emailRegex);

        if (email == null || !emailPattern.matcher(email).matches()) {
            log.error("Invalid email address format: " + email);
            throw new IncorrectEmailFormatException("Invalid email address format: " + email);
        }
    }

    public void validateEmail(final UserRequestDTO userRequestDTO) {
        if (userRepository.findByEmail(userRequestDTO.getEmail()).isPresent()) {
            log.error("User with email address " + userRequestDTO.getEmail() + " already exist.");
            throw new EmailAlreadyExistException("User with email address " + userRequestDTO.getEmail() + " already exist.");
        }
    }

}
