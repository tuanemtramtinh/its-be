package com.tuanemtramtinh.itsusers.dto;

import com.tuanemtramtinh.itscommon.enums.RoleEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
  private String token;
  private String email;
  private String firstName;
  private String lastName;
  private RoleEnum role;
}
