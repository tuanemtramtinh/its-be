package com.tuanemtramtinh.itslearningmanagement.dto;

import com.tuanemtramtinh.itscommon.enums.ContentStatusEnum;
import com.tuanemtramtinh.itscommon.enums.ContentTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContentRequest {
    private String courseInstanceId;
    private String title;
    private String description;
    private ContentTypeEnum type;
    private ContentStatusEnum status;
    private Integer orderIndex;
}
