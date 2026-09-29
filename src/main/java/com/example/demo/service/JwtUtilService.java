package com.example.demo.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtUtilService {

  @Value("${app.jwt.secret:TExBVkVfTVVZX1NFQ1JFVEE=}")
  private String jwtSecretKey;

  public static final long JWT_TOKEN_VALIDITY = Duration.ofMinutes(5).toMillis();

  public String extractUsername(String token) { 
    return extractClaim(token, Claims::getSubject); 
  }

  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    return claimsResolver.apply(extractAllClaims(token));
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser()
        .setSigningKey(jwtSecretKey)
        .parseClaimsJws(token)
        .getBody();
  }

  private boolean isTokenExpired(String token) {
    Instant expirationInstant = extractExpiration(token).toInstant();
    return expirationInstant.isBefore(Instant.now());
  }

  public String generateToken(UserDetails userDetails) {
      Map<String, Object> claims = new HashMap<>();
      String rol = userDetails.getAuthorities().stream()
          .map(a -> a.getAuthority())
          .findFirst().orElse("ROLE_USER");
      claims.put("rol", rol);
      return createToken(claims, userDetails.getUsername());
  }

  private String createToken(Map<String, Object> claims, String subject) {
    Instant now = Instant.now();
    Instant validity = now.plusMillis(JWT_TOKEN_VALIDITY);

    return Jwts.builder()
        .setClaims(claims)
        .setSubject(subject) // username
        .setIssuedAt(Date.from(now))
        .setExpiration(Date.from(validity))
        .signWith(SignatureAlgorithm.HS256, jwtSecretKey)
        .compact();
  }

  public boolean validateToken(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }
}