package com.tuanemtramtinh.itslearningmanagement.repositories;

import com.tuanemtramtinh.itscommon.entity.Course;
import com.tuanemtramtinh.itscommon.enums.CourseStatusEnum;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends MongoRepository<Course, String> {
  @Query("""
      {
        'status': ?0,
        $or: [
          { 'code':  { $regex: ?1, $options: 'i' } },
          { 'title': { $regex: ?1, $options: 'i' } }
        ]
      }
      """)
  Page<Course> searchByKeyword(CourseStatusEnum status, String regex, Pageable pageable);

  Page<Course> findByStatus(CourseStatusEnum status, Pageable pageable);
}
