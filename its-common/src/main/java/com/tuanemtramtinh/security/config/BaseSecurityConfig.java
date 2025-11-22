package com.tuanemtramtinh.security.config;

import com.tuanemtramtinh.security.filters.BaseJwtAuthenticationFilter;
import com.tuanemtramtinh.security.handlers.CustomAccessDeniedHandler;
import com.tuanemtramtinh.security.handlers.CustomAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

/**
 * Base Security Configuration for JWT authentication.
 * Services should extend this class and override getPublicEndpoints() to
 * specify public endpoints.
 */
@EnableWebSecurity
public abstract class BaseSecurityConfig {

  @Autowired
  protected BaseJwtAuthenticationFilter jwtAuthenticationFilter;

  @Autowired
  protected CustomAccessDeniedHandler customAccessDeniedHandler;

  @Autowired
  protected CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> {
          // Public endpoints
          String[] publicEndpoints = getPublicEndpoints();
          if (publicEndpoints != null && publicEndpoints.length > 0) {
            auth.requestMatchers(publicEndpoints).permitAll();
          }
          // All other requests require authentication
          auth.anyRequest().authenticated();
        })
        .exceptionHandling(exceptions -> exceptions
            .accessDeniedHandler(customAccessDeniedHandler)
            .authenticationEntryPoint(customAuthenticationEntryPoint))
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  /**
   * Override this method to specify public endpoints that don't require
   * authentication.
   * 
   * @return Array of public endpoint patterns (e.g., ["/register", "/login",
   *         "/"])
   */
  protected abstract String[] getPublicEndpoints();
}
