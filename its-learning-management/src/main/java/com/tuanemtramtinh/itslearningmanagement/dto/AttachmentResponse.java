package com.tuanemtramtinh.itslearningmanagement.dto;

import com.tuanemtramtinh.itscommon.enums.FileTypeEnum;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttachmentResponse {
    private String id;
    private String ownerId;
    private String fileUrl;
    private String fileName;
    private Long fileSize;
    private FileTypeEnum fileType;
    private Date uploadedAt;
}
