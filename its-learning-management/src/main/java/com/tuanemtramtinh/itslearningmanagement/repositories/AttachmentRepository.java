package com.tuanemtramtinh.itslearningmanagement.repositories;

import com.tuanemtramtinh.itscommon.entity.Attachment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentRepository extends MongoRepository<Attachment, String> {
    List<Attachment> findByOwnerId(String ownerId);
    void deleteByOwnerId(String ownerId);
}
