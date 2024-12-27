package com.shepherd.sheps_project.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtService {
    private final Key signingKey;
    @Value("${jwt_access_expiration}")
    private long accessTokenExpiration;
    @Value("${jwt_refresh_expiration}")
    private long refreshTokenExpiration;

    public String extractUsername(String jwtToken){
        return extractClaim(jwtToken, Claims::getSubject);
    }

    private <T> T extractClaim(String jwtToken, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(jwtToken);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String jwtToken) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    public String generateAccessToken(String email){
        return buildJwtToken(new HashMap<>(), email, accessTokenExpiration);
    }

    public String generateRefreshToken(String email){
        return buildJwtToken(new HashMap<>(), email, refreshTokenExpiration);
    }

    private String buildJwtToken(Map<String, Object> claims, String email, long tokenExpiration){
        Instant now = Instant.now();
        Date issuedAt = Date.from(now);
        Date expiredAt = Date.from(now.plusSeconds(tokenExpiration));
        return Jwts.builder()
                .setIssuer("Sheps App")
                .setIssuedAt(issuedAt)
                .setClaims(claims)
                .setSubject(email)
                .setExpiration(expiredAt)
                .signWith(signingKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean isValidToken(String token, String email){
        String username = extractUsername(token);
        return(username.equals(email)) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
