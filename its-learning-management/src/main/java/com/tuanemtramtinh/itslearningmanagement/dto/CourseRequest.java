package com.tuanemtramtinh.itslearningmanagement.dto;

import com.tuanemtramtinh.itscommon.enums.CourseStatusEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseRequest {
    private String title;
    private String code;
    private String description;
    private Integer credit;
    private CourseStatusEnum status;
}
