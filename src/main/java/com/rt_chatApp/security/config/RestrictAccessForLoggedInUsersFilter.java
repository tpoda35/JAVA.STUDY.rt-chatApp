package com.rt_chatApp.security.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Class for restricting users from specific routes/pages.
 */
@Component
public class RestrictAccessForLoggedInUsersFilter extends OncePerRequestFilter {

    /**
     * Method, which restricts authenticated user to access the login and register page.
     *
     * @param request incoming http request.
     * @param response for the http request.
     * @param filterChain Spring Security filter chain.
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String uri = request.getRequestURI();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (
                authentication != null &&
                authentication.isAuthenticated() &&
                !"anonymousUser".equals(authentication.getPrincipal()) &&
                ("/login.html".equals(uri) || "/register.html".equals(uri))
        ) {
            response.sendRedirect("/home");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
