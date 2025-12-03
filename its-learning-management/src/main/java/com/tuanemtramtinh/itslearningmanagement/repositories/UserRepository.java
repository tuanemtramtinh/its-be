package com.tuanemtramtinh.itslearningmanagement.repositories;

import com.tuanemtramtinh.itscommon.enums.RoleEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.tuanemtramtinh.itscommon.entity.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    boolean existsByIdAndListCourseInstanceContaining(String id, String courseInstanceId);

    Page<User> findAllByRoleAndListCourseInstanceContains(RoleEnum role, String courseInstanceId, Pageable pageable);

    Page<User> findAllByRole(RoleEnum role, Pageable pageable);

    @Query("""
            {
              'role': ?0,
              'listCourseInstance': { $ne: ?1 }
            }
            """)
    Page<User> findEligibleStudentsByRole(RoleEnum role, String courseInstanceId, Pageable pageable);
}
