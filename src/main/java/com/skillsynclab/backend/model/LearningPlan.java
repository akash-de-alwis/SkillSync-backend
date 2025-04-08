package com.skillsynclab.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "learning_plans")
public class LearningPlan {
    @Id
    private String id;
    private String title;
    private String description;
    private Author author;
    private List<String> topics;
    private String duration;
    private int followers;
    private LocalDateTime createdAt;
}