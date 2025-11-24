package com.tuanemtramtinh.itscommon.utils;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtUtil {

  @Value("${jwt.secret}")
  private String jwtSecret;

  @Value("${jwt.expiration}")
  private Long jwtExpiration;

  private SecretKey key;

  @PostConstruct
  public void init() {
    this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
  }

  public String generateToken(String username) {
    String token = Jwts.builder()
        .subject(username)
        .issuedAt(new Date())
        .expiration(new Date(new Date().getTime() + jwtExpiration))
        .signWith(key, Jwts.SIG.HS256) // Keys.hmacShaKeyFor() tự động sử dụng HS256
        .compact();
    return token;
  }

  public String generateToken(String userId, String username, String role) {
    String token = Jwts.builder()
        .subject(userId)
        .claim("username", username)
        .claim("role", role)
        .issuedAt(new Date())
        .expiration(new Date(new Date().getTime() + jwtExpiration))
        .signWith(key, Jwts.SIG.HS256) // Keys.hmacShaKeyFor() tự động sử dụng HS256
        .compact();
    return token;
  }

  // public String extractUsername(String token) {
  // return extractClaim(token, Claims::getSubject);
  // }

  // public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
  // final Claims claims = extractAllClaims(token);
  // return claimsResolver.apply(claims);
  // }

  // private Claims extractAllClaims(String token) {
  // return Jwts.parser()
  // .verifyWith(key)
  // .build()
  // .parseSignedClaims(token)
  // .getPayload();
  // }

  // public boolean validateToken(String token) {
  // return validateToken(token, null);
  // }

  // public boolean validateToken(String token, String username) {
  // try {
  // // Parse và verify signature
  // Jwts.parser().verifyWith(key).build().parseSignedClaims(token);

  // // Kiểm tra expiration
  // if (isTokenExpired(token)) {
  // String tokenUsername = extractUsername(token);
  // log.warn("JWT token expired for user: {}", tokenUsername != null ?
  // tokenUsername : "unknown");
  // return false;
  // }

  // // Nếu có username, kiểm tra username match
  // if (username != null) {
  // final String tokenUsername = extractUsername(token);
  // return username.equals(tokenUsername);
  // }

  // return true;
  // } catch (io.jsonwebtoken.ExpiredJwtException e) {
  // String tokenUsername = e.getClaims() != null ? e.getClaims().getSubject() :
  // "unknown";
  // log.warn("JWT token expired for user: {}", tokenUsername);
  // return false;
  // } catch (Exception e) {
  // if (username != null) {
  // log.error("JWT token validation failed for user: {}", username, e);
  // } else {
  // log.error("JWT validation error: {}", e.getMessage());
  // }
  // return false;
  // }
  // }

  // private boolean isTokenExpired(String token) {
  // return extractExpiration(token).before(new Date());
  // }

  // private Date extractExpiration(String token) {
  // return extractClaim(token, Claims::getExpiration);
  // }
}
