package com.tuanemtramtinh.itslearningmanagement.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.tuanemtramtinh.itscommon.entity.Course;
import com.tuanemtramtinh.itslearningmanagement.dto.CourseResponse;

@Mapper(componentModel = "spring")
public interface CourseResponseMapper {
  CourseResponse toDTO(Course entity);

  List<CourseResponse> toDTOList(List<Course> entities);
}
