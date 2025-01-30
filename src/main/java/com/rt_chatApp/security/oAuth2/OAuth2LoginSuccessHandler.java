package com.rt_chatApp.security.oAuth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rt_chatApp.security.config.JwtService;
import com.rt_chatApp.security.token.Token;
import com.rt_chatApp.security.token.TokenRepository;
import com.rt_chatApp.security.token.TokenType;
import com.rt_chatApp.security.user.User;
import com.rt_chatApp.security.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
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
    private final TokenRepository tokenRepository;

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

        Cookie accessToken = new Cookie("Authorization", jwt);
        accessToken.setHttpOnly(true);
        accessToken.setPath("/");
        accessToken.setMaxAge(30 * 60);
        response.addCookie(accessToken);

        Cookie refreshToken = new Cookie("RefreshToken", refresh);
        refreshToken.setHttpOnly(true);
        refreshToken.setPath("/");
        refreshToken.setMaxAge(30 * 24 * 60 * 60);
        response.addCookie(refreshToken);

        var token = Token.builder()
                .user(user)
                .token(jwt)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
        response.sendRedirect("/index.html");
    }
}
