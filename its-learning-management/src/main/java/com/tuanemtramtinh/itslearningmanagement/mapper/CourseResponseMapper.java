package com.tuanemtramtinh.itslearningmanagement.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import com.tuanemtramtinh.itscommon.entity.Course;
import com.tuanemtramtinh.itslearningmanagement.dto.CourseResponse;

@Mapper(componentModel = "spring")
public interface CourseResponseMapper {
  CourseResponse toDTO(Course entity);

  List<CourseResponse> toDTOList(List<Course> entities);

  default Page<CourseResponse> toDTOPage(Page<Course> entities) {
    // Page có sẵn hàm map(...) cực tiện
    return entities.map(this::toDTO);
  }
}
