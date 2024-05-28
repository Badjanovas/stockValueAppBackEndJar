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

    public void validateUserRequest(final UserRequestDTO userRequestDTO) throws MandatoryFieldsMissingException {
        if (userRequestDTO == null) {
            log.error("Request was empty.");
            throw new MandatoryFieldsMissingException("Request was empty.");
        } else if (userRequestDTO.getUserName() == null) {
            log.error("Mandatory user name field is missing.");
            throw new MandatoryFieldsMissingException("Mandatory user name field is missing.");
        } else if (userRequestDTO.getUserName().isBlank()) {
            log.error("Mandatory user name field is empty.");
            throw new MandatoryFieldsMissingException("Mandatory user name field is empty.");
        } else if (userRequestDTO.getPassword() == null) {
            log.error("Mandatory password field is missing.");
            throw new MandatoryFieldsMissingException("Mandatory password field is missing.");
        } else if (userRequestDTO.getPassword().isBlank()) {
            log.error("Mandatory password field is empty.");
            throw new MandatoryFieldsMissingException("Mandatory password field is empty.");
        } else if (userRequestDTO.getEmail() == null) {
            log.error("Mandatory email field is missing.");
            throw new MandatoryFieldsMissingException("Mandatory email field is missing.");
        } else if (userRequestDTO.getEmail().isBlank()) {
            log.error("Mandatory email field is empty.");
            throw new MandatoryFieldsMissingException("Mandatory email field is empty.");
        }
    }

    public void validateUserList(final List<User> users) throws NoUsersFoundException {
        if (users.isEmpty()) {
            log.error("No users were found in the DB!");
            throw new NoUsersFoundException("No users were found!");
        }
    }

    public void validateUserById(final Long id) throws NoUsersFoundException {
        if (!userRepository.existsById(id)) {
            log.error("User with id number " + id + " not found.");
            throw new NoUsersFoundException("User with id number " + id + " not found.");
        }
    }

    // Checks if the provided users username already exists in the system.
    public void validateUserName(final UserRequestDTO userRequestDTO) throws UserAlreadyExistException {
        final Optional<User> user = userRepository.findByUserName(userRequestDTO.getUserName());

        if (user.isPresent()) {
            log.error("User with username: " + userRequestDTO.getUserName() +
                    " already exist. Choose different user name.");
            throw new UserAlreadyExistException("User with username: " + userRequestDTO.getUserName() +
                    " already exist. Choose different user name.");
        }
    }

    public void validateEmailFormat(final String email) throws IncorrectEmailFormatException {
        final String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        final Pattern emailPattern = Pattern.compile(emailRegex);

        if (email == null || !emailPattern.matcher(email).matches()) {
            log.error("Invalid email address format: " + email);
            throw new IncorrectEmailFormatException("Invalid email address format: " + email);
        }
    }

    public void validateEmail(final UserRequestDTO userRequestDTO) throws EmailAlreadyExistException {
        if (userRepository.findByEmail(userRequestDTO.getEmail()).isPresent()){
            log.error("User with email address " + userRequestDTO.getEmail() + " already exist.");
            throw new EmailAlreadyExistException("User with email address " + userRequestDTO.getEmail() + " already exist.");
        }
    }

}
