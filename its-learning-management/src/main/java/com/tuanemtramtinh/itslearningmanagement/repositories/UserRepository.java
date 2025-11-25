package com.tuanemtramtinh.itslearningmanagement.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tuanemtramtinh.itscommon.entity.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

}
