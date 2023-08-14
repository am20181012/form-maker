package com.app.security2023.repository;

import com.app.security2023.model.Form;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FormRepository extends MongoRepository<Form, String> {

    List<Form> findAllByUserId(String id);
    boolean existsBySlug(String slug);
    boolean existsByImageUrl(String imageUrl);
    Optional<Form> findBySlug(String slug);
    Long countByUserId(String userId);
    Optional<Form> findTopByUserIdOrderByLatestUpdateDesc(String userId);
    List<Form> findByUserIdOrderByLatestUpdateDesc(String userId);
    List<Form> findByUserIdAndTitleLikeIgnoreCase(String userId, String title);
}
