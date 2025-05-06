package com.skillsynclab.backend.repository;


import com.skillsynclab.backend.model.Comment;
import com.skillsynclab.backend.model.SkillPost;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findByPost(SkillPost post);
}

