package com.tuanemtramtinh.itslearningmanagement.dto;

import com.tuanemtramtinh.itscommon.dto.UserResponse;
import com.tuanemtramtinh.itscommon.enums.CourseInstanceEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseInstanceResponse {
  private String id;
  private CourseResponse course;
  private UserResponse teacher;
  private CourseInstanceEnum status;

}
