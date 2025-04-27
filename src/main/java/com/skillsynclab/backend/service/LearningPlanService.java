package com.skillsynclab.backend.service;

import com.skillsynclab.backend.model.LearningPlan;
import com.skillsynclab.backend.repository.LearningPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LearningPlanService {
    @Autowired
    private LearningPlanRepository learningPlanRepository;

    public List<LearningPlan> getAllPlans() {
        return learningPlanRepository.findAll();
    }

    public Optional<LearningPlan> getPlanById(String id) {
        return learningPlanRepository.findById(id);
    }

    public LearningPlan createPlan(LearningPlan plan) {
        plan.setCreatedAt(LocalDateTime.now().toString());
        plan.setFollowers(0);
        return learningPlanRepository.save(plan);
    }

    public LearningPlan updatePlan(String id, LearningPlan plan) {
        LearningPlan existingPlan = learningPlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan not found"));
        existingPlan.setTitle(plan.getTitle());
        existingPlan.setDescription(plan.getDescription());
        existingPlan.setTopics(plan.getTopics());
        existingPlan.setDuration(plan.getDuration());
        existingPlan.setGoals(plan.getGoals());
        existingPlan.setResources(plan.getResources());
        existingPlan.setDifficulty(plan.getDifficulty());
        existingPlan.setPrerequisites(plan.getPrerequisites());
        return learningPlanRepository.save(existingPlan);
    }

    public void deletePlan(String id) {
        learningPlanRepository.deleteById(id);
    }
}