package jar.StockValueApp.service;


import jar.StockValueApp.dto.UserRequestDTO;
import jar.StockValueApp.dto.UserResponseDTO;
import jar.StockValueApp.exception.*;
import jar.StockValueApp.model.AuthenticationRequest;
import jar.StockValueApp.model.AuthenticationResponse;
import jar.StockValueApp.model.User;
import jar.StockValueApp.repository.UserRepository;
import jar.StockValueApp.service.mappingService.UserMappingService;
import jar.StockValueApp.validator.AuthenticationRequestValidator;
import jar.StockValueApp.validator.GlobalExceptionValidator;
import jar.StockValueApp.validator.UserRequestValidator;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@Data
public class UserService {

    private final UserRepository userRepository;
    private final UserMappingService userMappingService;
    private final UserRequestValidator userRequestValidator;
    private final GlobalExceptionValidator globalExceptionValidator;
    private final EmailSendingService emailSendingService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AuthenticationRequestValidator authenticationRequestValidator;

    public List<User> getAllUsers() throws NoUsersFoundException {
        log.info("Looking for users in DB.");
        final List<User> users = userRepository.findAll();
        userRequestValidator.validateUserList(users);
        log.info(users.size() + " users were found in DB.");
        return users;
    }

    public AuthenticationResponse register(final UserRequestDTO userRequestDTO)
            throws
            MandatoryFieldsMissingException,
            UserAlreadyExistException,
            IncorrectEmailFormatException,
            EmailAlreadyExistException,
            NotValidIdException
    {
        userRequestValidator.validateUserName(userRequestDTO);
        userRequestValidator.validateUserRequest(userRequestDTO);
        userRequestValidator.validateEmailFormat(userRequestDTO.getEmail());
        userRequestValidator.validateEmail(userRequestDTO);

        final User user = userMappingService.mapToEntity(userRequestDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        globalExceptionValidator.validateId(user.getId());
        emailSendingService.sendEmail(userRequestDTO.getEmail(), userRequestDTO);
        log.info("New user " + user.getUsername() + " was created and saved successfully.");
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request)
            throws MandatoryFieldsMissingException, NoUsersFoundException {
        authenticationRequestValidator.validateAuthenticationRequest(request);
        var user = userRepository.findByUserName(
                request.getUserName()).orElseThrow(() -> new NoUsersFoundException("Incorrect username or password.")
        );

        if(!passwordMatches(request.getPassword(), user.getPassword())){
            throw new NoUsersFoundException("Incorrect username or password.");
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUserName(),
                        request.getPassword()
                )
        );
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .user(user)
                .build();
    }

    public List<UserResponseDTO> deleteUserById(final Long id) throws NotValidIdException, NoUsersFoundException {
        globalExceptionValidator.validateId(id);
        userRequestValidator.validateUserById(id);
        userRepository.deleteById(id);
        log.info("User with id number " + id + " was deleted from DB successfully.");

        return userMappingService.mapToResponse(userRepository.findAll());
    }

    public User updateUserByUserName(final String userName, final UserRequestDTO user)
            throws
            NoUsersFoundException,
            UserAlreadyExistException,
            EmailAlreadyExistException,
            IncorrectEmailFormatException,
            MandatoryFieldsMissingException
    {
        userRequestValidator.validateUserRequest(user);
        User userToUpdate = userRepository.findByUserName(userName)
                .orElseThrow(() -> new NoUsersFoundException("User with username: " + userName + " was not found."));

        userRequestValidator.validateUserName(user);
        userRequestValidator.validateEmail(user);
        userRequestValidator.validateEmailFormat(user.getEmail());

        final boolean isUpdated = updateUserIfChanged(user, userToUpdate);
        if (isUpdated) {
            userRepository.save(userToUpdate);
            log.info("User " + userToUpdate.getUsername() + " was updated successfully.");
        }

        return userToUpdate;
    }

    private boolean updateUserIfChanged(final UserRequestDTO user, final User userToUpdate) {
        boolean isUpdated = false;

        if (!Objects.equals(userToUpdate.getUsername(), user.getUserName())) {
            userToUpdate.setUserName(user.getUserName());
            isUpdated = true;
        }
        if (!Objects.equals(userToUpdate.getPassword(), user.getPassword())) {
            userToUpdate.setPassword(user.getPassword());
            isUpdated = true;
        }
        if (!Objects.equals(userToUpdate.getEmail(), user.getEmail())) {
            userToUpdate.setEmail(user.getEmail());
            isUpdated = true;
        }
        return isUpdated;
    }

    public boolean passwordMatches(String plainTextPassword, String hashedPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(plainTextPassword, hashedPassword);
    }

}
