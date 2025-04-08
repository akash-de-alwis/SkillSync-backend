package com.skillsynclab.backend.controller;

import com.skillsynclab.backend.model.LearningProgress;
import com.skillsynclab.backend.service.LearningProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/learning-progress")
@CrossOrigin(origins = "http://localhost:3000")
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
    public LearningProgress createProgress(@RequestBody LearningProgress progress) {
        return learningProgressService.createProgress(progress);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LearningProgress> updateProgress(@PathVariable String id, @RequestBody LearningProgress progress) {
        return ResponseEntity.ok(learningProgressService.updateProgress(id, progress));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProgress(@PathVariable String id) {
        learningProgressService.deleteProgress(id);
        return ResponseEntity.ok().build();
    }
}