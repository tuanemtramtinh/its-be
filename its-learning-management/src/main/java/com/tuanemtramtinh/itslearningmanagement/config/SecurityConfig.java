package com.tuanemtramtinh.itslearningmanagement.config;

import com.tuanemtramtinh.security.config.BaseSecurityConfig;
import org.springframework.stereotype.Component;

@Component
public class SecurityConfig extends BaseSecurityConfig {

    @Override
    protected String[] getPublicEndpoints() {
        return new String[] { "/", "/actuator/**" };
    }
}
