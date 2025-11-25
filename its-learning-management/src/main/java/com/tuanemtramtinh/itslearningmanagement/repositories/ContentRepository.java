package com.tuanemtramtinh.itslearningmanagement.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tuanemtramtinh.itscommon.entity.Content;

@Repository
public interface ContentRepository extends MongoRepository<Content, String> {

}
