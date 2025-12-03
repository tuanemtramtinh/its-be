package com.tuanemtramtinh.itslearningmanagement.mapper;

import com.tuanemtramtinh.itscommon.entity.Content;
import com.tuanemtramtinh.itslearningmanagement.dto.ContentResponse;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ContentResponseMapper {
    ContentResponse toDTO(Content entity);

    List<ContentResponse> toDTOList(List<Content> entities);

    default Page<ContentResponse> toDTOPage(Page<Content> entities) {
        return entities.map(this::toDTO);
    }
}

