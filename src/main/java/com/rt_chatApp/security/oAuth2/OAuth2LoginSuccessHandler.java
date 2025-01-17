package com.rt_chatApp.security.oAuth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rt_chatApp.security.auth.AuthenticationResponse;
import com.rt_chatApp.security.config.JwtService;
import com.rt_chatApp.security.user.User;
import com.rt_chatApp.security.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository repository;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        OAuth2User userPrincipal = (OAuth2User) authentication.getPrincipal();
        User user = repository.findByEmail(userPrincipal.getName())
                .orElseThrow(() -> new EntityNotFoundException("User not found."));

        String jwt = jwtService.generateToken(user);
        String refresh = jwtService.generateRefreshToken(user);

        AuthenticationResponse authResponse = AuthenticationResponse.builder()
                .accessToken(jwt)
                .refreshToken(refresh)
                .build();

        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(authResponse));
    }
}
