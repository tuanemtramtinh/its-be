package com.tuanemtramtinh.itsusers.services;

import com.tuanemtramtinh.itscommon.entity.User;
import com.tuanemtramtinh.itscommon.enums.RoleEnum;
import com.tuanemtramtinh.itsusers.dto.LoginRequest;
import com.tuanemtramtinh.itsusers.dto.LoginResponse;
import com.tuanemtramtinh.itsusers.dto.RegisterRequest;
import com.tuanemtramtinh.itsusers.dto.RegisterResponse;
import com.tuanemtramtinh.itsusers.mapper.RegisterResponseMapper;
import com.tuanemtramtinh.itsusers.repositories.UserRepository;
import com.tuanemtramtinh.itscommon.utils.JwtUtil;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RegisterResponseMapper registerResponseMapper;

    public AuthenticationService(UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil, RegisterResponseMapper registerResponseMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.registerResponseMapper = registerResponseMapper;
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
                .role(req.getRole() != null ? req.getRole() : RoleEnum.STUDENT) // Default role
                .build();

        newUser = userRepository.save(newUser);

        return registerResponseMapper.toDTO(newUser);
    }

    public LoginResponse login(LoginRequest req) {
        User user = userRepository.findUserByEmail(req.getEmail());

        if (user == null) {
            throw new RuntimeException("Invalid email or password");
        }

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getEmail(),
                user.getRole() != null ? user.getRole() : RoleEnum.STUDENT);

        return new LoginResponse(
                token,
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole() != null ? user.getRole() : RoleEnum.STUDENT);
    }
}
