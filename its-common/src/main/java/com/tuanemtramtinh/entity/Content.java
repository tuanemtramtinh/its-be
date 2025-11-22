package com.tuanemtramtinh.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "contents")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Content {
    @Id
    private String id;

    private String courseInstanceId;

    private String title;
    private String description;
    private String type;
    private String status;
    private Integer orderIndex;

    private Date dueDate;
    private Date allowAt;
    private boolean allowedLate;

    private Date createdAt;
    private Date updatedAt;
}
