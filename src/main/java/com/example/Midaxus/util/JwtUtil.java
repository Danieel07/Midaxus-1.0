package com.example.midaxus.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Utility class for JSON Web Token (JWT) operations.
 * Handles token generation, validation, and claim extraction.
 */
@Component
public class JwtUtil {

  private final SecretKey key;
  private final long jwtExpiration;

  /**
   * Constructs JwtUtil with secret and expiration time.
   */
  public JwtUtil(@Value("${jwt.secret}") String secret, 
         @Value("${jwt.expiration}") long jwtExpiration) {
  this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  this.jwtExpiration = jwtExpiration;
  }

  /**
   * Generates a JWT token for a user.
   */
  public String generateToken(String username, String role, String email, String firstname) {
  return Jwts.builder()
    .setSubject(username)
    .claim("role", role)
    .claim("email", email)
    .claim("name", firstname)
    .setIssuedAt(new Date(System.currentTimeMillis()))
    .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
    .signWith(key, SignatureAlgorithm.HS256)
    .compact();
  }

  /**
   * Validates if the given token is authentic and not expired.
   */
  public boolean validateToken(String token) {
  try {
    Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    return true;
  } catch (Exception e) {
    return false;
  }
  }

  /**
   * Extracts the username (subject) from a token.
   */
  public String extractUsername(String token) {
  return extractClaim(token, Claims::getSubject);
  }

  /**
   * Extracts the user role from a token.
   */
  public String extractRole(String token) {
  return extractClaim(token, claims -> claims.get("role", String.class));
  }

  /**
   * Generic method to extract a specific claim from a token.
   */
  private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
  final Claims claims = Jwts.parserBuilder()
    .setSigningKey(key)
    .build()
    .parseClaimsJws(token)
    .getBody();
  return claimsResolver.apply(claims);
  }
}




















