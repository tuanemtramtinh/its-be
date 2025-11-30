package com.tuanemtramtinh.itslearningmanagement.mapper;

import com.tuanemtramtinh.itscommon.entity.CourseInstance;
import com.tuanemtramtinh.itslearningmanagement.dto.CourseInstanceUpdateStatusResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CourseInstanceUpdateStatusReponseMapper {
    @Mapping(source = "id", target = "courseInstanceId")
    @Mapping(source = "status", target = "courseInstanceStatus")
    CourseInstanceUpdateStatusResponse toDTO(CourseInstance entity);
}
