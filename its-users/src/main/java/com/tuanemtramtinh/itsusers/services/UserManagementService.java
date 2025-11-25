package com.tuanemtramtinh.itsusers.services;

import com.tuanemtramtinh.itscommon.dto.UserResponse;
import com.tuanemtramtinh.itscommon.entity.User;
import com.tuanemtramtinh.itsusers.mapper.UserResponseMapper;
import com.tuanemtramtinh.itsusers.repositories.UserRepository;

import java.util.List;

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

    public List<UserResponse> getUserList() {
        List<User> userList = this.userRepository.findAll();
        return userResponseMapper.toDTOList(userList);
    }

    public void deleteUser(String id) {
        if (id == null)
            throw new RuntimeException("UserId is null");
        userRepository.deleteById(id);
    }

}
