package com.app.security2023.repository;

import com.app.security2023.model.FormAnswer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormAnswerRepository extends MongoRepository<FormAnswer, String> {

    Long countByUserId(String userId);

//    List<FormAnswer> findByUserIdOrderByCreatedAtDesc(String userId);

}
