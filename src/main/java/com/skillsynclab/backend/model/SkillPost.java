package com.skillsynclab.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "skill_posts")
public class SkillPost {
    @Id
    private String id;
    private String title;
    private String description;
    private Author author;
    private int likes;
    private int comments;
    private LocalDateTime createdAt;
    private String image;
    private String category;
    private List<String> tags;
    private boolean allowComments;
    private String visibility; // "public", "followers", "private"
}