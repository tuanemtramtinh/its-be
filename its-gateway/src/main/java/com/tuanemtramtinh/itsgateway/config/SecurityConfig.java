package com.tuanemtramtinh.itsgateway.config;

import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebFluxSecurity
@Slf4j
public class SecurityConfig {

  @Value("${jwt.secret}")
  private String jwtSecret;

  @Autowired
  private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

  @Bean
  public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
    return http
        // .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .csrf(csrf -> csrf.disable())
        .authorizeExchange(exchanges -> exchanges
            .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            // Public endpoints - không cần authentication
            .pathMatchers("/users/register", "/users/login", "/actuator/**").permitAll()
            // Tất cả các request khác cần authentication
            .anyExchange().authenticated())
        .oauth2ResourceServer(oauth2 -> oauth2
            .jwt(Customizer.withDefaults())
            .authenticationEntryPoint(jwtAuthenticationEntryPoint))
        .exceptionHandling(exceptions -> exceptions
            .authenticationEntryPoint(jwtAuthenticationEntryPoint))
        .build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.setAllowedOriginPatterns(List.of("*")); // Use patterns for allowing all with credentials
    config.addAllowedHeader("*");
    config.addAllowedMethod("*");

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);

    // Return the source directly, let Spring Security wrap it
    return source;
  }

  @Bean
  public ReactiveJwtDecoder jwtDecoder() {
    byte[] secretBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
    if (secretBytes.length < 32) {
      log.warn("JWT secret is shorter than 32 bytes, may cause issues");
    }
    SecretKey key = Keys.hmacShaKeyFor(secretBytes);
    return NimbusReactiveJwtDecoder.withSecretKey(key).build();
  }
}
