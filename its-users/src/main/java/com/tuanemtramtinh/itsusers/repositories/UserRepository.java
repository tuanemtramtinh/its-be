package com.tuanemtramtinh.itsusers.repositories;

import com.tuanemtramtinh.itscommon.entity.User;
import com.tuanemtramtinh.itscommon.enums.RoleEnum;
import com.tuanemtramtinh.itscommon.enums.UserStatusEnum;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findUserByEmail(String email);

    boolean existsByEmail(String email);

    Page<User> findByRole(RoleEnum role, Pageable pageable);

    Page<User> findByStatus(UserStatusEnum status, Pageable pageable);

    Page<User> findByEmail(String email, Pageable pageable);

    Page<User> findByRoleAndEmail(RoleEnum role, String email, Pageable pageable);

    Page<User> findByStatusAndEmail(UserStatusEnum status, String email, Pageable pageable);

    Page<User> findByRoleAndStatus(RoleEnum role, UserStatusEnum status, Pageable pageable);

    Page<User> findByRoleAndStatusAndEmail(RoleEnum role, UserStatusEnum status, String email,
            Pageable pageable);

}
