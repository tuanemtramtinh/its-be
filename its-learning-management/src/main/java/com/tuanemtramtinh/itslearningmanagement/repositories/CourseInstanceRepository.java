package com.tuanemtramtinh.itslearningmanagement.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tuanemtramtinh.itscommon.entity.CourseInstance;

@Repository
public interface CourseInstanceRepository extends MongoRepository<CourseInstance, String> {
  boolean existsByCourseIdAndTeacherId(String courseId, String teacherId);

  Page<CourseInstance> findAllByTeacherId(String teacherId, Pageable pageable);
}
