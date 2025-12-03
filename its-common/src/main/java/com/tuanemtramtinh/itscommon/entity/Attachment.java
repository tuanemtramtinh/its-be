package com.tuanemtramtinh.itscommon.entity;

import com.tuanemtramtinh.itscommon.enums.FileTypeEnum;
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
@NoArgsConstructor
@AllArgsConstructor
public class Attachment {
    @Id
    private String id;

    private String ownerId;
    private String fileUrl;
    private String fileName;
    private Long fileSize;
    private FileTypeEnum fileType;

    private Date uploadedAt;
}
