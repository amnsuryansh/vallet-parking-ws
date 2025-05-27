package com.project.valetparking.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

/**
 * Filter for JWT token validation in OAuth2 context
 * 
 * This filter intercepts incoming requests, extracts and validates JWT tokens,
 * and sets up the security context for authenticated users.
 * It supports both access tokens and refresh tokens.
 */
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String token = resolveToken(request);

            // Skip token validation for token endpoints
            String requestPath = request.getRequestURI();
            if (requestPath.contains("/oauth2/token") || requestPath.contains("/api/auth/refresh-token")) {
                filterChain.doFilter(request, response);
                return;
            }

            if (token != null && jwtTokenProvider.validateToken(token)) {
                // Only process access tokens in the filter chain
                if (jwtTokenProvider.isAccessToken(token)) {
                    Authentication auth = jwtTokenProvider.getAuthentication(token);

                    // Check if the token has the required scopes for the request
                    if (hasRequiredScopes(request, token)) {
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    } else {
                        log.warn("Token does not have required scopes for the request");
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        response.getWriter().write("Insufficient scopes for this resource");
                        return;
                    }
                } else if (jwtTokenProvider.isRefreshToken(token)) {
                    // Refresh tokens should only be used at the token refresh endpoint
                    log.warn("Refresh token used for resource access");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Invalid token type");
                    return;
                }
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("Could not set user authentication in security context", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Authentication failed");
        }
    }

    /**
     * Extract token from request
     * 
     * @param request the HTTP request
     * @return the JWT token or null if not found
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * Check if the token has the required scopes for the request
     * 
     * @param request the HTTP request
     * @param token the JWT token
     * @return true if the token has the required scopes, false otherwise
     */
    private boolean hasRequiredScopes(HttpServletRequest request, String token) {
        Set<String> scopes = jwtTokenProvider.getScopes(token);
        String requestPath = request.getRequestURI();
        String method = request.getMethod();

        // Admin endpoints require admin scope
        if (requestPath.contains("/admin/")) {
            return scopes.contains("admin");
        }

        // Write operations require write scope
        if (method.equals("POST") || method.equals("PUT") || method.equals("DELETE") || method.equals("PATCH")) {
            return scopes.contains("write") || scopes.contains("admin");
        }

        // Read operations require read scope
        return scopes.contains("read") || scopes.contains("write") || scopes.contains("admin");
    }
}
