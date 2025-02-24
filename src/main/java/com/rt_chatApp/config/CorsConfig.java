package com.rt_chatApp.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

/**
 * Cors configuration, which allows origins to access this app.
 *
 * <p>This class implements {@link CorsConfigurationSource}
 * to ensure the access to the application.</p>
 */

@Component
public class CorsConfig implements CorsConfigurationSource {

    /**
     * Sets up the configuration.
     *
     * @param request the incoming request.
     * @return the fully configured configuration.
     */
    @Override
    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:8080"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        config.setAllowedHeaders(List.of("*"));
        return config;
    }
}
