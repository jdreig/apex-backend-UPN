// com.example.demo.service.JwtUtilService
package com.example.demo.service;

import io.jsonwebtoken.Claims;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.*;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtUtilService {

  // MEJOR: lee de application.properties (fallback a tu valor base64 si no está seteado)
  @Value("${app.jwt.secret:TExBVkVfTVVZX1NFQ1JFVEE=}")
  private String JWT_SECRET_KEY;

  public static final long JWT_TOKEN_VALIDITY = java.time.Duration.ofMinutes(5).toMillis();

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
        .setSigningKey(JWT_SECRET_KEY)
        .parseClaimsJws(token)
        .getBody();
  }

  private Boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
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
    return Jwts.builder()
        .setClaims(claims)
        .setSubject(subject) // username
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
        .signWith(SignatureAlgorithm.HS256, JWT_SECRET_KEY)
        .compact();
  }

  public boolean validateToken(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }
}
