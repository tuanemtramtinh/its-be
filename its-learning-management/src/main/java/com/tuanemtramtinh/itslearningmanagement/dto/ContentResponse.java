package com.tuanemtramtinh.itslearningmanagement.dto;

import com.tuanemtramtinh.itscommon.enums.ContentStatusEnum;
import com.tuanemtramtinh.itscommon.enums.ContentTypeEnum;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContentResponse {
    private String id;
    private String courseInstanceId;
    private String title;
    private String description;
    private ContentTypeEnum type;
    private ContentStatusEnum status;
    private Integer orderIndex;
    private Date createdAt;
    private Date updatedAt;
}
