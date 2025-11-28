package com.tuanemtramtinh.itscommon.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.tuanemtramtinh.itscommon.enums.CourseStatusEnum;

import java.util.Date;

@Document(collection = "courses")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    @Id
    private String id;

    private String title;
    private String code;
    private String description;
    private Integer credit;
    private CourseStatusEnum status;

    private Date createdAt;
    private Date updatedAt;
}
