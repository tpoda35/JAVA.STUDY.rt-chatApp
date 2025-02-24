package com.rt_chatApp.security.config;

import com.rt_chatApp.security.token.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

/**
 * Custom JWT filter class.
 *
 * <p>Gets called at every request, because it extends {@link OncePerRequestFilter}.
 * Ensures that the JWT is always in the header, and it's not expired.</p>
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;
  private final TokenRepository tokenRepository;

  /**
   * This resolver sends the exceptions to the global handler {@link com.rt_chatApp.controller.ExceptionController}.
   */
  private final HandlerExceptionResolver resolver;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserDetailsService userDetailsService,
            TokenRepository tokenRepository,
            @Qualifier("handlerExceptionResolver")
            HandlerExceptionResolver resolver) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.tokenRepository = tokenRepository;
        this.resolver = resolver;
    }

  /**
   * Method, which checks the JWT.
   *
   * <p>If the path contains /api/v1/auth, then we don't need to filter,
   * because we have the authentication endpoints there.
   * The filter gets the cookies from the header, saves the JWT in a variable if it's found.
   * Extract the email, and then checks it and the auth context if it is null.
   * Checks if the token is valid. Sets the auth context and switches to the next filter.</p>
   *
   * @param request incoming http request.
   * @param response of the incoming http request.
   * @param filterChain the Spring Security filter chain.
   */
    @Override
    protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain
  ) throws ServletException, IOException {
    try {
      if (request.getServletPath().contains("/api/v1/auth")) {
        filterChain.doFilter(request, response);
        return;
      }

      String jwt = null;
      final String userEmail;

      // Save the cookies in an Array, and we loop through them to find the Authorization cookie.
      // Then we save that to the jwt variable.
      Cookie[] cookies = request.getCookies();
      if (cookies != null){
        for (Cookie cookie : cookies){
          if ("Authorization".equals(cookie.getName())){
            jwt = cookie.getValue();
            break;
          }
        }
      }

      if (jwt == null) {
        filterChain.doFilter(request, response);
        return;
      }

      userEmail = jwtService.extractUsername(jwt);
      if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
        var isTokenValid = tokenRepository.findByToken(jwt)
                .map(t -> !t.isExpired() && !t.isRevoked())
                .orElse(false);
        if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
          UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                  userDetails,
                  null,
                  userDetails.getAuthorities()
          );
          authToken.setDetails(
                  new WebAuthenticationDetailsSource().buildDetails(request)
          );
          SecurityContextHolder.getContext().setAuthentication(authToken);
        }
      }
      filterChain.doFilter(request, response);
    } catch (Exception e) {
      resolver.resolveException(request, response, null, e);
    }
  }
}
