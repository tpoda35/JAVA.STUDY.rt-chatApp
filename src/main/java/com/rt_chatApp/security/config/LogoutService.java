package com.rt_chatApp.security.config;

import com.rt_chatApp.exception.LogoutException;
import com.rt_chatApp.security.token.TokenRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerExceptionResolver;

/**
 * Service class for logout.
 */
@Service
public class LogoutService implements LogoutHandler {

  private final TokenRepository tokenRepository;

  /**
   * This resolver sends the exceptions to the global handler {@link com.rt_chatApp.controller.ExceptionController}.
   */
  private final HandlerExceptionResolver resolver;

    public LogoutService(
            TokenRepository tokenRepository,
            @Qualifier("handlerExceptionResolver")
            HandlerExceptionResolver resolver) {
        this.tokenRepository = tokenRepository;
        this.resolver = resolver;
    }

  /**
   * Method, which logs out the user. (will be reformated)
   *
   * <p>Gets the cookies from the header, saves the JWT.
   * Sets the JWT expiration and validation to false.
   * Removes the cookies and clears the auth context.</p>
   *
   * @param request incoming http request.
   * @param response of the incoming http request.
   * @param authentication user authentication object.
   */
  @Override
  @Transactional
  public void logout(
          HttpServletRequest request,
          HttpServletResponse response,
          Authentication authentication
  ) {
      try {
        String jwt = null;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
          for (Cookie cookie : cookies) {
            if ("Authorization".equals(cookie.getName())) {
              jwt = cookie.getValue();
              break;
            }
          }
        }

        if (jwt == null) {
          SecurityContextHolder.clearContext();
          throw new EntityNotFoundException("Missing JWT from header.");
        }

        var storedToken = tokenRepository.findByToken(jwt)
                .orElse(null);
        if (storedToken != null) {
          storedToken.setExpired(true);
          storedToken.setRevoked(true);
          tokenRepository.save(storedToken);

          for (Cookie cookie : cookies){
            cookie.setMaxAge(0);
            cookie.setValue(null);
            cookie.setPath("/");
            response.addCookie(cookie);
          }
        } else {
          throw new LogoutException("Logout failed.");
        }
      } catch (Exception e){
        resolver.resolveException(request,response, null, e);
      }
    }
}
