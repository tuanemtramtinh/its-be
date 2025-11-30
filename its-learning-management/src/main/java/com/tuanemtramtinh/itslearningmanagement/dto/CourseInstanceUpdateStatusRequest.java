package com.tuanemtramtinh.itslearningmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseInstanceUpdateStatusRequest {
    String courseInstanceId;
    String newStatus;
}
