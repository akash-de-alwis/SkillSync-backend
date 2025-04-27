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
        progress.setCreatedAt(LocalDateTime.now().toString());
        return learningProgressRepository.save(progress);
    }

    public LearningProgress updateProgress(String id, LearningProgress progress) {
        LearningProgress existingProgress = learningProgressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Progress not found"));
        existingProgress.setTitle(progress.getTitle());
        existingProgress.setDescription(progress.getDescription());
        existingProgress.setProgressPercent(progress.getProgressPercent());
        existingProgress.setMilestone(progress.getMilestone());
        existingProgress.setTemplate(progress.getTemplate());
        existingProgress.setSkillsGained(progress.getSkillsGained());
        existingProgress.setChallengesFaced(progress.getChallengesFaced());
        existingProgress.setNextSteps(progress.getNextSteps());
        existingProgress.setEvidenceLink(progress.getEvidenceLink());
        return learningProgressRepository.save(existingProgress);
    }

    public void deleteProgress(String id) {
        learningProgressRepository.deleteById(id);
    }
}