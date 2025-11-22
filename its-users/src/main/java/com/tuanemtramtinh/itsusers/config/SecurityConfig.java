package com.tuanemtramtinh.itsusers.config;

import com.tuanemtramtinh.security.config.BaseSecurityConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig extends BaseSecurityConfig {

  @Override
  protected String[] getPublicEndpoints() {
    return new String[] { "/register", "/login", "/" };
  }
}
