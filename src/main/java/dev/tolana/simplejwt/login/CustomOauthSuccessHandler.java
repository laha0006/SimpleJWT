package dev.tolana.simplejwt.login;

import dev.tolana.simplejwt.user.AppUser;
import dev.tolana.simplejwt.user.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class CustomOauthSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    @Transactional
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        String email = oauthToken.getPrincipal().getAttribute("email");
        String username = oauthToken.getPrincipal().getAttribute("login");

         AppUser user = userRepository.findByEmail(email).orElseGet(() -> {
            AppUser newUser = new AppUser();
            newUser.setEmail(email);
            newUser.setUsername(username);
            return userRepository.save(newUser);
        });


         String jwt = tokenService.createJwtToken(user);
         response.getWriter().write("{\"token\":\"" + jwt + "\"}");

    }
}
