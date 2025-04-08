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
        plan.setCreatedAt(LocalDateTime.now());
        return learningPlanRepository.save(plan);
    }

    public LearningPlan updatePlan(String id, LearningPlan planDetails) {
        LearningPlan plan = learningPlanRepository.findById(id).orElseThrow();
        plan.setTitle(planDetails.getTitle());
        plan.setDescription(planDetails.getDescription());
        plan.setAuthor(planDetails.getAuthor());
        plan.setTopics(planDetails.getTopics());
        plan.setDuration(planDetails.getDuration());
        plan.setFollowers(planDetails.getFollowers());
        return learningPlanRepository.save(plan);
    }

    public void deletePlan(String id) {
        learningPlanRepository.deleteById(id);
    }
}