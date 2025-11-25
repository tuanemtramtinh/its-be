package com.tuanemtramtinh.itscommon.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.tuanemtramtinh.itscommon.enums.CourseInstanceEnum;

import java.util.Date;

@Document(collection = "course-instances")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseInstance {
    @Id
    private String id;

    private String courseId;
    private String teacherId;

    // private String startDate;
    // private String endDate;
    private CourseInstanceEnum status;

    private Date createdAt;
    private Date updatedAt;
}
