package co.jordan.boldauthentic.service.implementations;


import co.jordan.boldauthentic.dto.LoginDto;
import co.jordan.boldauthentic.dto.LoginResponseDto;
import co.jordan.boldauthentic.dto.RegisterDto;
import co.jordan.boldauthentic.model.User;
import co.jordan.boldauthentic.model.enums.Role;
import co.jordan.boldauthentic.repository.UserRepository;
import co.jordan.boldauthentic.utils.ApiResponse;
import co.jordan.boldauthentic.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }


    @Override
    public UserDetails loadUserByUsername(String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }

    public User registerUser(RegisterDto request) {
        if (userRepository.findByLogin(request.getLogin()).isPresent()) {
            throw new RuntimeException("Ce login est déjà utilisé");
        }

        User user = User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .login(request.getLogin())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        return userRepository.save(user);
    }

    public LoginResponseDto authenticateUser(LoginDto loginDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getLogin(), loginDto.getPassword())
        );

        User user = userRepository.findByLogin(loginDto.getLogin())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        String token = jwtUtil.generateToken(user.getLogin());
        return new LoginResponseDto(token, user.getRole().name());
    }

}
