package com.tuanemtramtinh.itsusers.dto;

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
  private String role;
}
