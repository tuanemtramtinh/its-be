package com.tuanemtramtinh.security.filters;

import com.tuanemtramtinh.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Base class for JWT Authentication Filter.
 * Services can extend this class and override methods if needed.
 */
public abstract class BaseJwtAuthenticationFilter extends OncePerRequestFilter {

  @Autowired
  protected JwtUtil jwtUtil;

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain chain)
      throws ServletException, IOException {

    final String authorizationHeader = request.getHeader("Authorization");

    String email = null;
    String jwt = null;
    String role = null;

    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      jwt = authorizationHeader.substring(7);
      try {
        // Extract email từ token (subject trong JWT là email)
        email = jwtUtil.extractUsername(jwt);
        role = jwtUtil.extractClaim(jwt, claims -> claims.get("role", String.class));

        // Validate token với email (chỉ cần 1 lần)
        // Nếu không hợp lệ, reset về null
        if (!jwtUtil.validateToken(jwt, email)) {
          email = null;
          role = null;
        }
      } catch (Exception e) {
        logger.error("JWT token validation failed", e);
        email = null;
        role = null;
      }
    }

    if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      List<SimpleGrantedAuthority> authorities = Collections.singletonList(
          new SimpleGrantedAuthority("ROLE_" + (role != null ? role : getDefaultRole())));

      UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
          email, null, authorities);
      authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      SecurityContextHolder.getContext().setAuthentication(authToken);
    }
    chain.doFilter(request, response);
  }

  /**
   * Override this method to provide custom default role.
   * Default implementation returns "USER".
   */
  protected String getDefaultRole() {
    return "USER";
  }
}
