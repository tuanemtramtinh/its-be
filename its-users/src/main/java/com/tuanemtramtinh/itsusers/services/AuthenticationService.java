package com.tuanemtramtinh.itsusers.services;

import com.tuanemtramtinh.itscommon.entity.User;
import com.tuanemtramtinh.itsusers.dto.LoginRequest;
import com.tuanemtramtinh.itsusers.dto.LoginResponse;
import com.tuanemtramtinh.itsusers.dto.RegisterRequest;
import com.tuanemtramtinh.itsusers.dto.RegisterResponse;
import com.tuanemtramtinh.itsusers.repositories.UserRepository;
import com.tuanemtramtinh.itscommon.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthenticationService(UserRepository userRepository,
                                 PasswordEncoder passwordEncoder,
                                 JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public RegisterResponse register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        String encodedPassword = passwordEncoder.encode(req.getPassword());
        User newUser = User.builder()
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .email(req.getEmail())
                .password(encodedPassword)
                .role("USER") // Default role
                .build();
        newUser = userRepository.save(newUser);
        return new RegisterResponse(newUser.getId(), newUser.getEmail());
    }

    public LoginResponse login(LoginRequest req) {
        User user = userRepository.findUserByEmail(req.getEmail());

        if (user == null) {
            throw new RuntimeException("Invalid email or password");
        }

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole() != null ? user.getRole() : "USER");

        return new LoginResponse(
                token,
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole() != null ? user.getRole() : "USER");
    }
}
