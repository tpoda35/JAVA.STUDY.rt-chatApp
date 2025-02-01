package com.rt_chatApp.security.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService service;

  // Registers a user.
  // Will be modified in the future (mostly the Refresh Token system).
  // Process:
  // server.register(request) registers the user.
  // If everything is good, then it gives back a jwt + refresh token.
  // These two gets saved in HttpOnly Cookies.
  @PostMapping("/register")
  public ResponseEntity<String> register(
      @RequestBody @Valid RegisterRequest request,
      HttpServletResponse response
  ) {
    AuthenticationResponse authResponse = service.register(request);

    Cookie accessToken = new Cookie("Authorization", authResponse.getAccessToken());
    accessToken.setHttpOnly(true);
    accessToken.setPath("/");
    accessToken.setMaxAge(30 * 60); // 30 min
    response.addCookie(accessToken);

    Cookie refreshToken = new Cookie("RefreshToken", authResponse.getRefreshToken());
    refreshToken.setHttpOnly(true);
    refreshToken.setPath("/");
    refreshToken.setMaxAge(30 * 24 * 60 * 60); // 30 day
    response.addCookie(refreshToken);

    return ResponseEntity.ok("Registered and authenticated.");
  }

  // Authenticates the user.
  // Process:
  // The service.authenticate(request) authenticates the user.
  // If it was successful, then we have a JWT and a RefreshToken in the authResponse variable.
  // We add them to two different HttpOnly Cookies and save them to the response.
  @PostMapping("/authenticate")
  public ResponseEntity<String> authenticate(
      @RequestBody AuthenticationRequest request,
      HttpServletResponse response
  ) {
    AuthenticationResponse authResponse = service.authenticate(request);

    Cookie accessToken = new Cookie("Authorization", authResponse.getAccessToken());
    accessToken.setHttpOnly(true);
    accessToken.setPath("/");
    accessToken.setMaxAge(30 * 60); // 30 min
    response.addCookie(accessToken);

    Cookie refreshToken = new Cookie("RefreshToken", authResponse.getRefreshToken());
    refreshToken.setHttpOnly(true);
    refreshToken.setPath("/");
    refreshToken.setMaxAge(30 * 24 * 60 * 60); // 30 day
    response.addCookie(refreshToken);

    return ResponseEntity.ok("Authenticated.");
  }

  // Refreshes the access token.
  // Process:
  // The service.refreshToken(refreshToken) gives back a new JWT token.
  // If the accessToken is null gives back an AuthenticationException which is handled with the global exception handler.
  // If the accessToken is not null, then it creates a new HttpOnly Cookie and save it to the response.
  @PostMapping("/refresh-token")
  public ResponseEntity<String> refreshToken(
        @CookieValue("RefreshToken") String refreshToken,
        HttpServletResponse response
  ) throws IOException {
    String accessToken = service.refreshToken(refreshToken);
    if (accessToken == null){
      throw new AuthenticationException("Session expired. Login again.");
    }

    Cookie accessTokenCookie = new Cookie("Authorization", accessToken);
    accessTokenCookie.setHttpOnly(true);
    accessTokenCookie.setPath("/");
    accessTokenCookie.setMaxAge(30 * 60); // 30 min
    response.addCookie(accessTokenCookie);

    return ResponseEntity.ok("Authenticated.");
  }
}
