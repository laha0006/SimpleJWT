package dev.tolana.simplejwt.login;

import dev.tolana.simplejwt.user.AppUser;
import dev.tolana.simplejwt.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String githubEmail = oAuth2User.getAttribute("email");
        String githubUsername = oAuth2User.getAttribute("login");

        // Check if user exists in the database by email
        Optional<AppUser> existingUser = userRepository.findByUsername(githubUsername);
        if (existingUser.isEmpty()) {
            // Create new AppUser if it doesn't exist
            AppUser newUser = AppUser.builder()
                    .username(githubUsername)
                    .email(githubEmail)
                    .firstName(oAuth2User.getAttribute("name"))
                    .password("")  // OAuth2 users usually don't have a local password
                    .build();
            userRepository.save(newUser);
        }

        return oAuth2User;
    }
}