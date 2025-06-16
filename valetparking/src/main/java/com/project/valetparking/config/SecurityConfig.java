package com.project.valetparking.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Spring Security configuration with OAuth2 support
 * 
 * This class configures security settings for the application, including:
 * - Authentication and authorization rules
 * - JWT token filter
 * - CORS configuration
 * - Password encoding
 * - Authentication manager
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Value("${oauth2.client-id:valet-parking-client}")
    private String clientId;

    @Value("${oauth2.client-secret}")
    private String clientSecret;

    /**
     * Configure security filter chain with OAuth2 support
     * 
     * This method sets up the security rules for the application:
     * - Disables CSRF for REST API
     * - Configures stateless session management
     * - Sets up authorization rules for different endpoints
     * - Adds JWT token filter for authentication
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Public endpoints that don't require authentication
                .requestMatchers("/v1/auth/**", "/v1/admin/host/register", "/oauth2/token").permitAll()

                // Host endpoints require specific roles
    //                    .requestMatchers("/v1/admin/host/**").hasAnyRole("HOST_MASTER", "HOST_ADMIN", "HOST_EMPLOYEE")

                // Host user creation endpoints require admin roles
                .requestMatchers("/v1/host-users/create").hasAnyRole("SUPERADMIN", "HOSTADMIN", "HOSTUSER")

                // Admin endpoints require admin role
                .requestMatchers("/v1/admin/**").hasRole("SUPERADMIN")

                // All other endpoints require authentication
                .anyRequest().authenticated()
            )
            // Add JWT token filter before the standard authentication filter
            .addFilterBefore(new JwtTokenFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configure CORS for cross-origin requests
     * 
     * This allows the API to be accessed from different origins (domains)
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
        configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Password encoder for secure password storage
     * 
     * BCrypt is a strong hashing algorithm suitable for password storage
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Authentication manager for processing authentication requests
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
