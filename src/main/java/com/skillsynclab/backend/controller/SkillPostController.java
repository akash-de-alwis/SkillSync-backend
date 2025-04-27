package com.skillsynclab.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillsynclab.backend.model.Author;
import com.skillsynclab.backend.model.SkillPost;
import com.skillsynclab.backend.service.SkillPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/skill-posts")
@CrossOrigin(origins = "http://localhost:8080", allowCredentials = "true")
public class SkillPostController {
    @Autowired
    private SkillPostService skillPostService;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping
    public List<SkillPost> getAllPosts() {
        return skillPostService.getAllPosts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SkillPost> getPostById(@PathVariable String id) {
        return skillPostService.getPostById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<SkillPost> createPost(
            @RequestPart("post") String postJson,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @AuthenticationPrincipal OAuth2User principal) {
        try {
            if (postJson == null || postJson.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(null);
            }

            SkillPost post = objectMapper.readValue(postJson, SkillPost.class);

            Author author = new Author();
            author.setName(principal.getAttribute("name"));
            author.setAvatar(principal.getAttribute("picture"));
            post.setAuthor(author);

            if (image != null && !image.isEmpty()) {
                if (image.getSize() > 5 * 1024 * 1024) {
                    return ResponseEntity.status(400).body(null);
                }
                String base64Image = java.util.Base64.getEncoder().encodeToString(image.getBytes());
                String imageData = "data:image/jpeg;base64," + base64Image;
                post.setImage(imageData);
            }

            SkillPost createdPost = skillPostService.createPost(post);
            return ResponseEntity.ok(createdPost);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<SkillPost> updatePost(
            @PathVariable String id,
            @RequestPart("post") String postJson,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @AuthenticationPrincipal OAuth2User principal) {
        try {
            if (postJson == null || postJson.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(null);
            }

            SkillPost post = objectMapper.readValue(postJson, SkillPost.class);

            SkillPost existingPost = skillPostService.getPostById(id)
                    .orElseThrow(() -> new RuntimeException("Post not found"));

            String userName = principal.getAttribute("name");
            if (!existingPost.getAuthor().getName().equals(userName)) {
                return ResponseEntity.status(403).body(null);
            }

            // Preserve existing image if no new image is provided
            if (image != null && !image.isEmpty()) {
                if (image.getSize() > 5 * 1024 * 1024) {
                    return ResponseEntity.status(400).body(null);
                }
                String base64Image = java.util.Base64.getEncoder().encodeToString(image.getBytes());
                String imageData = "data:image/jpeg;base64," + base64Image;
                post.setImage(imageData);
            } else {
                post.setImage(existingPost.getImage());
            }

            post.setAuthor(existingPost.getAuthor());

            SkillPost updatedPost = skillPostService.updatePost(id, post);
            return ResponseEntity.ok(updatedPost);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(
            @PathVariable String id,
            @AuthenticationPrincipal OAuth2User principal) {
        try {
            SkillPost existingPost = skillPostService.getPostById(id)
                    .orElseThrow(() -> new RuntimeException("Post not found"));

            String userName = principal.getAttribute("name");
            if (!existingPost.getAuthor().getName().equals(userName)) {
                return ResponseEntity.status(403).build();
            }

            skillPostService.deletePost(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<SkillPost> toggleLike(
            @PathVariable String id,
            @AuthenticationPrincipal OAuth2User principal) {
        try {
            String userId = principal.getAttribute("sub");
            SkillPost updatedPost = skillPostService.toggleLike(id, userId);
            return ResponseEntity.ok(updatedPost);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/auth/check")
    public ResponseEntity<Map<String, String>> checkAuth(@AuthenticationPrincipal OAuth2User principal) {
        Map<String, String> response = new HashMap<>();
        if (principal != null) {
            response.put("status", "Authenticated");
            response.put("email", principal.getAttribute("email"));
            response.put("name", principal.getAttribute("name"));
            response.put("picture", principal.getAttribute("picture"));
            response.put("sub", principal.getAttribute("sub"));
            return ResponseEntity.ok(response);
        }
        response.put("status", "Not authenticated");
        return ResponseEntity.status(401).body(response);
    }
}