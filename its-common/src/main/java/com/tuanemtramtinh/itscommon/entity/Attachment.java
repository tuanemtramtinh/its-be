package com.tuanemtramtinh.itscommon.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "attachments")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Attachment {
    @Id
    private String id;

    private String ownerId;
    private String courseId;
    private String fileUrl;
    private String fileName;
    private Long fileSize;
    private String fileType;
    private Date uploadedAt;
}
