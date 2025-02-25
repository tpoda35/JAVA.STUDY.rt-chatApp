package com.rt_chatApp.security.oAuth2;

import com.rt_chatApp.security.user.User;
import com.rt_chatApp.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import static com.rt_chatApp.security.user.AuthProvider.GOOGLE;
import static com.rt_chatApp.security.user.Role.USER;
import static com.rt_chatApp.security.user.Status.OFFLINE;

/**
 * Service class for handling register/login with OAuth2.
 */
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository repository;

    /**
     * Method, which handles the OAuth2 external logins.
     *
     * <p>Gets the data from the external service. Checks if there's a user with that data.
     * If there's no user with the data, creates a new user and saves it to the database.</p>
     *
     * @param userRequest is the request what the user sent in with the external login service.
     * @return {@link DefaultOAuth2User} with the user data.
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        String displayName = oAuth2User.getAttribute("given_name");

        repository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .email(email)
                            .displayName(displayName)
                            .authProvider(GOOGLE)
                            .role(USER)
                            .status(OFFLINE)
                            .build();

                    return repository.save(newUser);
                });

        return new DefaultOAuth2User(oAuth2User.getAuthorities(), oAuth2User.getAttributes(), "email");
    }
}
