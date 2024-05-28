package jar.StockValueApp.service;


import jar.StockValueApp.repository.UserRepository;
import jar.StockValueApp.service.mappingService.UserMappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final UserRepository repository;
    private final UserMappingService mappingService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;


}
