package co.jordan.boldauthentic.controller;

import co.jordan.boldauthentic.dto.LoginDto;
import co.jordan.boldauthentic.dto.LoginResponseDto;
import co.jordan.boldauthentic.dto.RegisterDto;
import co.jordan.boldauthentic.model.User;
import co.jordan.boldauthentic.service.implementations.UserService;
import co.jordan.boldauthentic.utils.ApiResponse;
import co.jordan.boldauthentic.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthentificationController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register( @RequestBody RegisterDto request) {
        userService.registerUser(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Utilisateur enregistré avec succès", null, HttpStatus.CREATED));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(@RequestBody LoginDto request) {
        LoginResponseDto response = userService.authenticateUser(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("Connexion réussie", response, HttpStatus.OK));
    }

}
