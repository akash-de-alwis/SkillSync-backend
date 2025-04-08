package com.skillsynclab.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String email;
    private String name;
    private String avatar;
    private String provider;
    private String providerId;
    private String title;
    private String location;
    private String bio;
    private List<String> skills;
    private String github;
    private String linkedin;
    private Stats stats;

    @Data
    public static class Stats {
        private int posts;
        private int plans;
        private int following;
        private int followers;
    }
}