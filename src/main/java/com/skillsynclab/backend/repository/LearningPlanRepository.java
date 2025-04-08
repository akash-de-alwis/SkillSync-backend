package com.skillsynclab.backend.repository;

import com.skillsynclab.backend.model.LearningPlan;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LearningPlanRepository extends MongoRepository<LearningPlan, String> {
}