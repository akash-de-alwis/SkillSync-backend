package com.skillsynclab.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "learning_progress")
public class LearningProgress {
    @Id
    private String id;
    private String title;
    private String description;
    private int progressPercent;
    private String milestone;
    private Author author;
    private LocalDateTime createdAt;
}