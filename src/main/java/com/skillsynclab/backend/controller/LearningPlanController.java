// plans  done
package com.skillsynclab.backend.controller;

import com.skillsynclab.backend.model.LearningPlan;
import com.skillsynclab.backend.model.Author;
import com.skillsynclab.backend.service.LearningPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/learning-plans")
@CrossOrigin(origins = "http://localhost:8080") // Update to match frontend port
public class LearningPlanController {
    @Autowired
    private LearningPlanService learningPlanService;

    @GetMapping
    public List<LearningPlan> getAllPlans() {
        return learningPlanService.getAllPlans();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LearningPlan> getPlanById(@PathVariable String id) {
        return learningPlanService.getPlanById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<LearningPlan> createPlan(
            @RequestBody LearningPlan plan,
            @AuthenticationPrincipal OAuth2User principal) {
        try {
            Author author = new Author();
            author.setName(principal.getAttribute("name"));
            author.setAvatar(principal.getAttribute("picture"));
            plan.setAuthor(author);
            LearningPlan createdPlan = learningPlanService.createPlan(plan);
            return ResponseEntity.ok(createdPlan);
        } catch (Exception e) {
            System.err.println("Error creating learning plan: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<LearningPlan> updatePlan(
            @PathVariable String id,
            @RequestBody LearningPlan plan,
            @AuthenticationPrincipal OAuth2User principal) {
        try {
            LearningPlan existingPlan = learningPlanService.getPlanById(id)
                    .orElseThrow(() -> new RuntimeException("Plan not found"));
            String userName = principal.getAttribute("name");
            if (!existingPlan.getAuthor().getName().equals(userName)) {
                return ResponseEntity.status(403).build(); // Forbidden
            }
            plan.setAuthor(existingPlan.getAuthor());
            LearningPlan updatedPlan = learningPlanService.updatePlan(id, plan);
            return ResponseEntity.ok(updatedPlan);
        } catch (Exception e) {
            System.err.println("Error updating learning plan: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlan(
            @PathVariable String id,
            @AuthenticationPrincipal OAuth2User principal) {
        try {
            LearningPlan existingPlan = learningPlanService.getPlanById(id)
                    .orElseThrow(() -> new RuntimeException("Plan not found"));
            String userName = principal.getAttribute("name");
            if (!existingPlan.getAuthor().getName().equals(userName)) {
                return ResponseEntity.status(403).build(); // Forbidden
            }
            learningPlanService.deletePlan(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.err.println("Error deleting learning plan: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }
}