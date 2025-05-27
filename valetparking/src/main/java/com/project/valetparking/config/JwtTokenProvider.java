package com.project.valetparking.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Component for JWT token generation and validation in OAuth2 context
 * 
 * This class handles the creation and validation of JWT tokens used for authentication
 * and authorization in the OAuth2 flow. It supports both access tokens and refresh tokens.
 */
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret:defaultSecretKeyThatShouldBeChangedInProduction}")
    private String secretKey;

    @Value("${jwt.expiration:86400000}") // 24 hours in milliseconds
    private long accessTokenValidityInMilliseconds;

    @Value("${jwt.refresh-token.expiration:604800000}") // 7 days in milliseconds
    private long refreshTokenValidityInMilliseconds;

    @Value("${oauth2.client-id:valet-parking-client}")
    private String clientId;

    private Key key;

    @PostConstruct
    protected void init() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    /**
     * Create a JWT access token for an authenticated user
     * 
     * @param authentication the authentication object containing user details and authorities
     * @param scopes the OAuth2 scopes to include in the token
     * @return the JWT access token string
     */
    public String createAccessToken(Authentication authentication, Set<String> scopes) {
        String username = authentication.getName();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date now = new Date();
        Date validity = new Date(now.getTime() + accessTokenValidityInMilliseconds);

        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .claim("scopes", String.join(" ", scopes))
                .claim("client_id", clientId)
                .claim("token_type", "access_token")
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Create a JWT refresh token for an authenticated user
     * 
     * @param username the username of the authenticated user
     * @return the JWT refresh token string
     */
    public String createRefreshToken(String username) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + refreshTokenValidityInMilliseconds);

        return Jwts.builder()
                .setSubject(username)
                .claim("token_type", "refresh_token")
                .claim("client_id", clientId)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Get authentication from token
     * 
     * @param token the JWT token
     * @return the Authentication object
     */
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        String username = claims.getSubject();
        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("roles", String.class).split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // Return standard authentication
        UserDetails userDetails = new User(username, "", authorities);
        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }

    /**
     * Validate token
     * 
     * @param token the JWT token to validate
     * @return true if the token is valid, false otherwise
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Extract username from token
     * 
     * @param token the JWT token
     * @return the username from the token
     */
    public String getUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Extract token type from token
     * 
     * @param token the JWT token
     * @return the token type (access_token or refresh_token)
     */
    public String getTokenType(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("token_type", String.class);
    }

    /**
     * Get token expiration date
     * 
     * @param token the JWT token
     * @return the expiration date of the token
     */
    public Date getExpirationDate(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }
    
    /**
     * Check if a token is a refresh token
     * 
     * @param token the JWT token
     * @return true if the token is a refresh token, false otherwise
     */
    public boolean isRefreshToken(String token) {
        return "refresh_token".equals(getTokenType(token));
    }
    
    /**
     * Check if a token is an access token
     * 
     * @param token the JWT token
     * @return true if the token is an access token, false otherwise
     */
    public boolean isAccessToken(String token) {
        return "access_token".equals(getTokenType(token));
    }
    
    /**
     * Get scopes from token
     * 
     * @param token the JWT token
     * @return the set of scopes from the token, or an empty set if none
     */
    public Set<String> getScopes(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            
            if (claims.containsKey("scopes")) {
                return Arrays.stream(claims.get("scopes", String.class).split(" "))
                        .collect(Collectors.toSet());
            }
            return Collections.emptySet();
        } catch (Exception e) {
            return Collections.emptySet();
        }
    }
}