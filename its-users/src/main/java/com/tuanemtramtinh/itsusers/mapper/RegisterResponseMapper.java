package com.tuanemtramtinh.itsusers.mapper;

import org.mapstruct.Mapper;

import com.tuanemtramtinh.itscommon.entity.User;
import com.tuanemtramtinh.itsusers.dto.RegisterResponse;

@Mapper(componentModel = "spring")
public interface RegisterResponseMapper {
  RegisterResponse toDTO(User user);
}
