package com.tuanemtramtinh.itslearningmanagement.dto;

import com.tuanemtramtinh.itscommon.enums.FileTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttachmentRequest {
    private String fileUrl;
    private String fileName;
    private Long fileSize;
    private FileTypeEnum fileType;
}
