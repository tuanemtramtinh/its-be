package com.tuanemtramtinh.itslearningmanagement.mapper;

import com.tuanemtramtinh.itscommon.entity.Attachment;
import com.tuanemtramtinh.itslearningmanagement.dto.AttachmentResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AttachmentResponseMapper {
    AttachmentResponse toDTO(Attachment entity);

    List<AttachmentResponse> toDTOList(List<Attachment> entities);
}

