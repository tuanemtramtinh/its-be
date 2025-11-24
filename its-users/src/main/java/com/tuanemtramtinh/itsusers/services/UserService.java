package com.tuanemtramtinh.itsusers.services;

import org.springframework.stereotype.Service;

import com.tuanemtramtinh.itscommon.entity.User;
import com.tuanemtramtinh.itsusers.dto.UserRequest;
import com.tuanemtramtinh.itsusers.dto.UserResponse;
import com.tuanemtramtinh.itsusers.mapper.UserResponseMapper;
import com.tuanemtramtinh.itsusers.repositories.UserRepository;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final UserResponseMapper userResponseMapper;

  public UserService(UserRepository userRepository, UserResponseMapper userResponseMapper) {
    this.userRepository = userRepository;
    this.userResponseMapper = userResponseMapper;
  }

  public UserResponse getCurrentUserInfo(String userId) {
    User currentUser = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    return userResponseMapper.toDTO(currentUser);
  }

  public UserResponse updateCurrentUserInfo(String userId, UserRequest data) {

    if (userId == null)
      throw new RuntimeException("UserId is null");

    User currentUser = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

    if (data.getEmail() != null)
      currentUser.setEmail(data.getEmail());
    if (data.getFirstName() != null)
      currentUser.setFirstName(data.getFirstName());
    if (data.getLastName() != null)
      currentUser.setLastName(data.getLastName());
    if (data.getRole() != null)
      currentUser.setRole(data.getRole());

    currentUser = userRepository.save(currentUser);
    return userResponseMapper.toDTO(currentUser);

  }

}
