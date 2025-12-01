package com.tuanemtramtinh.itscommon.dto;

import com.tuanemtramtinh.itscommon.enums.RoleEnum;
import com.tuanemtramtinh.itscommon.enums.UserStatusEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
  private String id;
  private String firstName;
  private String lastName;
  private String email;
  private RoleEnum role;
  private UserStatusEnum status;
}
