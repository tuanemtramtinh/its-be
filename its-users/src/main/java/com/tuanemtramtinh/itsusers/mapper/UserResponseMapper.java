package com.tuanemtramtinh.itsusers.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.tuanemtramtinh.itscommon.entity.User;
import com.tuanemtramtinh.itsusers.dto.UserResponse;

@Mapper(componentModel = "spring")
public interface UserResponseMapper {
  UserResponse toDTO(User entity);

  List<UserResponse> toDTOList(List<User> entities);
}
