package com.skillsynclab.backend.model;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;

@Data
@Document(collection = "comments")
public class Comment {
    @Id
    private String id;
    private String content;
    private Author author;
    private LocalDateTime createdAt;
    @DBRef
    private SkillPost post; // Reference to the associated SkillPost
}