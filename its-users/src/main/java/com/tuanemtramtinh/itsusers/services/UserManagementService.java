package com.tuanemtramtinh.itsusers.services;

import com.tuanemtramtinh.itscommon.dto.UserResponse;
import com.tuanemtramtinh.itscommon.entity.User;
import com.tuanemtramtinh.itscommon.enums.RoleEnum;
import com.tuanemtramtinh.itscommon.enums.UserStatusEnum;
import com.tuanemtramtinh.itsusers.mapper.UserResponseMapper;
import com.tuanemtramtinh.itsusers.repositories.UserRepository;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class UserManagementService {

    private final UserRepository userRepository;
    private final UserResponseMapper userResponseMapper;

    public UserManagementService(UserRepository userRepository, UserResponseMapper userResponseMapper) {
        this.userRepository = userRepository;
        this.userResponseMapper = userResponseMapper;
    }

    public UserResponse getUserDetail(String userId) {

        if (userId == null)
            throw new RuntimeException("UserId is null");

        User currentUser = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User Not Found"));
        return userResponseMapper.toDTO(currentUser);
    }

    public Page<UserResponse> getUserList(String email, RoleEnum role, UserStatusEnum status,
            @NonNull Pageable pageable) {
        User probe = new User();
        probe.setEmail(email);
        probe.setRole(role);
        probe.setStatus(status);

        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues().withIgnoreCase()
                .withMatcher("email", match -> match.contains()).withMatcher("role", match -> match.exact())
                .withMatcher("status", match -> match.exact());

        Example<User> example = Example.of(probe, matcher);

        Page<User> userList = userRepository.findAll(example, pageable);

        return userResponseMapper.toDTOPage(userList);
    }

    public void deleteUser(String id) {
        if (id == null)
            throw new RuntimeException("UserId is null");
        userRepository.deleteById(id);
    }

}
