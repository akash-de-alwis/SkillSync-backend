package com.skillsynclab.backend.controller;

import com.skillsynclab.backend.model.LearningProgress;
import com.skillsynclab.backend.model.Author;
import com.skillsynclab.backend.service.LearningProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/learning-progress")
@CrossOrigin(origins = "http://localhost:8080")
public class LearningProgressController {
    @Autowired
    private LearningProgressService learningProgressService;

    @GetMapping
    public List<LearningProgress> getAllProgress() {
        return learningProgressService.getAllProgress();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LearningProgress> getProgressById(@PathVariable String id) {
        return learningProgressService.getProgressById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<LearningProgress> createProgress(
            @RequestBody LearningProgress progress,
            @AuthenticationPrincipal OAuth2User principal) {
        try {
            Author author = new Author();
            author.setName(principal.getAttribute("name"));
            author.setAvatar(principal.getAttribute("picture"));
            progress.setAuthor(author);
            LearningProgress createdProgress = learningProgressService.createProgress(progress);
            return ResponseEntity.ok(createdProgress);
        } catch (Exception e) {
            System.err.println("Error creating learning progress: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<LearningProgress> updateProgress(
            @PathVariable String id,
            @RequestBody LearningProgress progress,
            @AuthenticationPrincipal OAuth2User principal) {
        try {
            LearningProgress existingProgress = learningProgressService.getProgressById(id)
                    .orElseThrow(() -> new RuntimeException("Progress not found"));
            String userName = principal.getAttribute("name");
            if (!existingProgress.getAuthor().getName().equals(userName)) {
                return ResponseEntity.status(403).build(); // Forbidden
            }
            progress.setAuthor(existingProgress.getAuthor());
            LearningProgress updatedProgress = learningProgressService.updateProgress(id, progress);
            return ResponseEntity.ok(updatedProgress);
        } catch (Exception e) {
            System.err.println("Error updating learning progress: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProgress(
            @PathVariable String id,
            @AuthenticationPrincipal OAuth2User principal) {
        try {
            LearningProgress existingProgress = learningProgressService.getProgressById(id)
                    .orElseThrow(() -> new RuntimeException("Progress not found"));
            String userName = principal.getAttribute("name");
            if (!existingProgress.getAuthor().getName().equals(userName)) {
                return ResponseEntity.status(403).build(); // Forbidden
            }
            learningProgressService.deleteProgress(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.err.println("Error deleting learning progress: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }
}