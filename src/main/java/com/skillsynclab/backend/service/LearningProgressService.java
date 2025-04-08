package com.skillsynclab.backend.service;

import com.skillsynclab.backend.model.LearningProgress;
import com.skillsynclab.backend.repository.LearningProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LearningProgressService {
    @Autowired
    private LearningProgressRepository learningProgressRepository;

    public List<LearningProgress> getAllProgress() {
        return learningProgressRepository.findAll();
    }

    public Optional<LearningProgress> getProgressById(String id) {
        return learningProgressRepository.findById(id);
    }

    public LearningProgress createProgress(LearningProgress progress) {
        progress.setCreatedAt(LocalDateTime.now());
        return learningProgressRepository.save(progress);
    }

    public LearningProgress updateProgress(String id, LearningProgress progressDetails) {
        LearningProgress progress = learningProgressRepository.findById(id).orElseThrow();
        progress.setTitle(progressDetails.getTitle());
        progress.setDescription(progressDetails.getDescription());
        progress.setProgressPercent(progressDetails.getProgressPercent());
        progress.setMilestone(progressDetails.getMilestone());
        progress.setAuthor(progressDetails.getAuthor());
        return learningProgressRepository.save(progress);
    }

    public void deleteProgress(String id) {
        learningProgressRepository.deleteById(id);
    }
}