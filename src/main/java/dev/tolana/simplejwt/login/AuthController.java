package dev.tolana.simplejwt.login;

import dev.tolana.simplejwt.user.AppUser;
import dev.tolana.simplejwt.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;


    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody RegisterRequestDto registerDto) {
        AppUser user = AppUser.builder()
                .username(registerDto.userName())
                .password(passwordEncoder.encode(registerDto.password()))
                .firstName(registerDto.firstName())
                .build();

        Optional<AppUser> userOptional = userRepository.findByUsername(user.getUsername());
        if (userOptional.isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        userRepository.save(user);

        String token = tokenService.createJwtToken(user);
        return ResponseEntity.ok(new AuthResponseDto(token));

    }

//    @PostMapping("/login")
//    public ResponseEntity<Object> login(@RequestBody LoginRequestDto loginRequestDto) {
//        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.username(), loginRequestDto.password()));
//    }

    @GetMapping("/profile")
    public String profile(Authentication authentication) {
        return authentication.getName();
    }
}
