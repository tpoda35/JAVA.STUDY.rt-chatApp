package com.rt_chatApp.security.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Custom entry point for the application.
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * Method, which redirects the user or gives back a status code if
     * it's unauthenticated.
     *
     * @param request sent in request.
     * @param response the response what this method gives back.
     * @param authException the exception which the user got.
     */
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        String requestUri = request.getRequestURI();

        if (requestUri.startsWith("/api")){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized API Access");
        } else {
            response.sendRedirect("/login.html");
        }
    }
}
