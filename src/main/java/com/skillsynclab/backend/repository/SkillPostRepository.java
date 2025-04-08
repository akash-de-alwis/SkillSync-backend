package com.skillsynclab.backend.repository;

import com.skillsynclab.backend.model.SkillPost;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SkillPostRepository extends MongoRepository<SkillPost, String> {
}