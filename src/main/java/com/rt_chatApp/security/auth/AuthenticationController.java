package com.rt_chatApp.security.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService service;

  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponse> register(
      @RequestBody RegisterRequest request
  ) {
    return ResponseEntity.ok(service.register(request));
  }

  @PostMapping("/authenticate")
  public ResponseEntity<?> authenticate(
      @RequestBody AuthenticationRequest request,
      HttpServletResponse response
  ) {
    AuthenticationResponse authenticationResponse = service.authenticate(request);

    // Access Token
    Cookie accessToken = new Cookie("Authorization", authenticationResponse.getAccessToken());
    accessToken.setHttpOnly(true);
    accessToken.setPath("/");
    accessToken.setMaxAge(30 * 60); // 30 min
    response.addCookie(accessToken);

    Cookie refreshToken = new Cookie("RefreshToken", authenticationResponse.getRefreshToken());
    refreshToken.setHttpOnly(true);
    refreshToken.setPath("/");
    refreshToken.setMaxAge(30 * 24 * 60 * 60); // 30 day
    response.addCookie(refreshToken);

    return ResponseEntity.ok("Authenticated");
  }

  @PostMapping("/refresh-token")
  public void refreshToken(
      HttpServletRequest request,
      HttpServletResponse response
  ) throws IOException {
    service.refreshToken(request, response);
  }


}
