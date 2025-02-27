package com.rt_chatApp.security.config;

import com.rt_chatApp.security.oAuth2.CustomOAuth2UserService;
import com.rt_chatApp.security.oAuth2.OAuth2LoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * Configuration class for the Spring Security.
 *
 * <p>Enables Web Security and also method level security.</p>
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    /**
     * List of the endpoints which doesn't require authentication.
     */
    private static final String[] WHITE_LIST_URL = {
            "/api/v1/auth/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html",
            "/oauth2/**",
            "/css/**",
            "/js/**",
            "/images/**",
            "/login.html",
            "/register.html"
    };
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    /**
     * Configures the application's security filter chain.
     *
     * <p>This configuration defines the core Spring Security behavior, including:</p>
     * <ul>
     *     <li>Disabling CSRF protection.</li>
     *     <li>Permitting requests to specified endpoints and securing all other endpoints.</li>
     *     <li>Handling authentication exceptions with a custom entry point.</li>
     *     <li>Setting session management to stateless to ensure JWT-based authentication.</li>
     *     <li>Integrating OAuth2 login with custom user service and success handler.</li>
     *     <li>Adding a JWT authentication filter before the username-password filter.</li>
     *     <li>Configuring logout behavior with a custom logout handler.</li>
     * </ul>
     *
     * @param http the {@link HttpSecurity} instance to configure.
     * @return the configured {@link SecurityFilterChain}.
     * @throws Exception if an error occurs while building the security configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers(WHITE_LIST_URL).permitAll()
                                .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> { exception
                    .authenticationEntryPoint(authenticationEntryPoint);
                })
                .securityContext(context -> { context
                        .requireExplicitSave(false);
                })
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .oauth2Login(oAuth2 ->
                        oAuth2
                                .userInfoEndpoint(userInfoEndpointConfig ->
                                        userInfoEndpointConfig.userService(customOAuth2UserService)
                                )
                                .successHandler(oAuth2LoginSuccessHandler)
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout ->
                        logout
                                .logoutUrl("/api/v1/auth/logout")
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler((request, response, authentication) -> {
                                    SecurityContextHolder.clearContext();
                                    response.sendRedirect("/login.html");
                                })
                );

        return http.build();
    }
}
