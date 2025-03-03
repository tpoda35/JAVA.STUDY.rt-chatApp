package com.rt_chatApp.security.auth;

import com.rt_chatApp.exception.UserNotFoundException;
import com.rt_chatApp.security.config.JwtService;
import com.rt_chatApp.security.token.Token;
import com.rt_chatApp.security.token.TokenRepository;
import com.rt_chatApp.security.user.User;
import com.rt_chatApp.security.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;

import static com.rt_chatApp.security.token.TokenType.BEARER;
import static com.rt_chatApp.security.user.AuthProvider.LOCAL;
import static com.rt_chatApp.security.user.Role.USER;
import static com.rt_chatApp.security.user.Status.OFFLINE;

/**
 * Service class for the authentication system.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final UserRepository repository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  /**
   * Method, which registers a new user.
   *
   * <p>Checks if the email is in use, if it's not then sets up
   * and saves a new user object.</p>
   *
   * @param request sent in used data.
   */
  @Transactional
  public void register(RegisterRequest request) {
    if (repository.existsByEmail(request.getEmail())){
      throw new BadCredentialsException("Email already in use.");
    }

    var user = User.builder()
            .displayName(request.getDisplayName())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(USER)
            .authProvider(LOCAL)
            .status(OFFLINE)
            .build();

    var savedUser = repository.save(user);
  }

  /**
   * Method, which authenticates a user.
   *
   * <p>Creates a new auth object, generates JWT and Refresh tokens,
   * saves it and sends it back.</p>
   *
   * @param request sent in user data.
   * @return {@link AuthenticationResponse} for the cookies.
   */
  @Transactional
  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()
        )
    );
    var user = repository.findByEmail(request.getEmail())
            .orElseThrow(() -> new UserNotFoundException("User not found."));
    var jwtToken = jwtService.generateToken(user);
    // Will be modified to stay the same for 30 days.
    var refreshToken = jwtService.generateRefreshToken(user);
    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);
    return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .build();
  }

  @Transactional
  protected void saveUserToken(User user, String jwtToken) {
    var token = Token.builder()
        .user(user)
        .token(jwtToken)
        .tokenType(BEARER)
        .expired(false)
        .revoked(false)
        .build();
    tokenRepository.save(token);
  }

  @Transactional
  protected void revokeAllUserTokens(User user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  /**
   * Method, which refreshes the JWT token. (Will be modified)
   *
   * <p>Extracts the email from the refresh token,
   * gets the user from the database with the email,
   * with the user generates a new JWT token.</p>
   *
   * @param refreshToken from the header.
   * @return access token string.
   */
  @Transactional
  public String refreshToken(
          String refreshToken
  ) throws IOException {
    final String userEmail;
    if (refreshToken == null) {
      throw new AuthenticationException("Missing refresh token. Login again.");
    }

    userEmail = jwtService.extractUsername(refreshToken);
    if (userEmail != null) {
      var user = this.repository.findByEmail(userEmail)
              .orElseThrow();
      if (jwtService.isTokenValid(refreshToken, user)) {
        var accessToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);

        return accessToken;
      }
    }
    return null;
  }

  public boolean userExists(String email){
    return repository.findByEmail(email).isPresent();
  }
}
