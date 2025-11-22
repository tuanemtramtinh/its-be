package com.tuanemtramtinh.itslearningmanagement.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseResponse {
    private String id;
    private String title;
    private String code;
    private String description;
    private String credit;
    private String status;
}
