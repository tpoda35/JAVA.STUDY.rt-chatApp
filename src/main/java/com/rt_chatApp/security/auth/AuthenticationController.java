package com.rt_chatApp.security.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;

/**
 * Controller class for the user authentication system.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService service;

  /**
   * Endpoint for registering.
   *
   * @param request sent in user data.
   * @return a string with 200 status code. (will be changed)
   */
  @PostMapping("/register")
  public ResponseEntity<String> register(
      @RequestBody @Valid RegisterRequest request
  ) {
    service.register(request);
    return ResponseEntity.ok("Registered and authenticated.");
  }

  /**
   * Endpoint for logging in.
   *
   * <p>Authenticates the user, and if everything goes fine,
   * sets up 2 httponly cookie with the JWT and Refresh token and send it back.</p>
   *
   * @param request sent in user data.
   * @param response which will be sent back.
   * @return string with 200 status code. (will be changed)
   */
  @PostMapping("/authenticate")
  public ResponseEntity<String> authenticate(
      @RequestBody @Valid AuthenticationRequest request,
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

  /**
   * Endpoint for the refresh token system (not works right now, will be fixed).
   *
   * @param refreshToken the saved refresh token from the header.
   * @param response which will be sent back.
   * @return string with 200 status code. (will be changed)
   */
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
