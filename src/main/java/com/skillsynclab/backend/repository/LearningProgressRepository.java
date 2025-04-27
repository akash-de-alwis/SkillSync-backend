package com.skillsynclab.backend.repository;


import com.skillsynclab.backend.model.LearningProgress;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LearningProgressRepository extends MongoRepository<LearningProgress, String> {
}