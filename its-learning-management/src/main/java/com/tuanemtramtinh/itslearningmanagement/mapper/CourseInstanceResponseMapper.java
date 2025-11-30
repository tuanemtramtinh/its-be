package com.tuanemtramtinh.itslearningmanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.tuanemtramtinh.itscommon.entity.Course;
import com.tuanemtramtinh.itscommon.entity.CourseInstance;
import com.tuanemtramtinh.itscommon.entity.User;
import com.tuanemtramtinh.itslearningmanagement.dto.CourseInstanceResponse;

@Mapper(componentModel = "spring")
public interface CourseInstanceResponseMapper {

  @Mapping(target = "id", source = "entity.id")
  @Mapping(target = "status", source = "entity.status")
  @Mapping(target = "course", source = "course")
  @Mapping(target = "teacher", source = "teacher")
  CourseInstanceResponse toDTO(CourseInstance entity, Course course, User teacher);
}
