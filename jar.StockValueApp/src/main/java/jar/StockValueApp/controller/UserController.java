package jar.StockValueApp.controller;


import jar.StockValueApp.dto.UserRequestDTO;
import jar.StockValueApp.exception.*;
import jar.StockValueApp.model.AuthenticationRequest;
import jar.StockValueApp.model.AuthenticationResponse;
import jar.StockValueApp.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = {
        "http://localhost:4200",
        "http://stockvalueapp.s3-website.eu-west-2.amazonaws.com",
})
public class UserController {

    private final UserService userService;

    @GetMapping("/")
    public ResponseEntity<?> findAll() throws NoUsersFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsers());
    }

    @PostMapping("/")
    public ResponseEntity<?> addUser(@RequestBody final UserRequestDTO userRequestDTO)
            throws MandatoryFieldsMissingException,
            UserAlreadyExistException,
            IncorrectEmailFormatException,
            EmailAlreadyExistException,
            NotValidIdException {
        return ResponseEntity.status(HttpStatus.OK).body(userService.register(userRequestDTO));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable final Long userId) throws NotValidIdException, NoUsersFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(userService.deleteUserById(userId));
    }

    @PutMapping("/update/{userName}")
    public ResponseEntity<?> updateUser(@PathVariable final String userName, @RequestBody final UserRequestDTO user)
            throws NoUsersFoundException,
            UserAlreadyExistException,
            EmailAlreadyExistException,
            IncorrectEmailFormatException,
            MandatoryFieldsMissingException {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUserByUserName(userName, user));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request)
            throws MandatoryFieldsMissingException, NoUsersFoundException {
        return ResponseEntity.ok(userService.authenticate(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody UserRequestDTO request)
            throws UserAlreadyExistException,
            IncorrectEmailFormatException,
            NotValidIdException,
            MandatoryFieldsMissingException,
            EmailAlreadyExistException {
        return ResponseEntity.ok(userService.register(request));
    }

}
