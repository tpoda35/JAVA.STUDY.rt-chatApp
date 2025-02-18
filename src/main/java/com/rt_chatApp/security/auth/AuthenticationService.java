package com.rt_chatApp.security.auth;

import com.rt_chatApp.security.config.JwtService;
import com.rt_chatApp.security.token.Token;
import com.rt_chatApp.security.token.TokenRepository;
import com.rt_chatApp.security.user.User;
import com.rt_chatApp.security.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
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

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final UserRepository repository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  // Registers the user.
  // Process:
  // User gives the data with the RegisterRequest object, with that data a new user object is created with the lombok builder.
  // The user object saved in the DB, also generates JWT and refreshToken with the user object.
  // Only the JWT token getting saved to the DB, which later will be authenticated with every request.
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

  // Authenticates/Logins a user.
  // Process:
  // User gives in the data with the AuthenticationRequest object, which is passed to the authenticationManager.
  // That authenticates the user.
  // With the user object the code creates a new JWT and refreshToken.
  // Only the JWT token getting saved to the DB, which later will be authenticated with every request.
  // Gives back response in the form of AuthenticationResponse.
  @Transactional
  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()
        )
    );
    var user = repository.findByEmail(request.getEmail())
            .orElseThrow(() -> new EntityNotFoundException("User not found."));
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

  // Saves the JWT token to the DB.
  // Process:
  // The method need a user object and a JWT token.
  // A new Token object is created with that data and saved to the database.
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

  // Revokes all the valid tokens related to the given user.
  // Process:
  // The method need a user object.
  // The code gets all the valid user token, where the Expiration && Revoked is not true and modifies them to true.
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

  // Better exception handling will be added.
  // Gives back a new httponly cookie with a new jwt token, if the refresh token is valid.
  // Else logs out the user.
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
