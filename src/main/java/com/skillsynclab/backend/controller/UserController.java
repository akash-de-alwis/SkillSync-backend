package com.skillsynclab.backend.controller;

import com.skillsynclab.backend.model.User;
import com.skillsynclab.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:8080")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        try {
            User user = userService.getUserByEmail(email);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            System.out.println("User not found for email: " + email);
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{email}")
    public ResponseEntity<User> updateUser(@PathVariable String email, @RequestBody User updatedUser) {
        try {
            User user;
            try {
                // Try to fetch existing user
                user = userService.getUserByEmail(email);
            } catch (RuntimeException e) {
                // If user doesn't exist, create a new one
                user = new User();
                user.setEmail(email);
                user.setName(updatedUser.getName() != null ? updatedUser.getName() : "Unknown User"); // From auth
                user.setAvatar(updatedUser.getAvatar() != null ? updatedUser.getAvatar() : ""); // From auth
                user.setProvider("google"); // Assume Google OAuth
                user.setProviderId(updatedUser.getProviderId() != null ? updatedUser.getProviderId() : "unknown"); // Fallback
                user.setStats(new User.Stats());
                System.out.println("Creating new user for email: " + email);
            }

            // Update editable fields
            user.setTitle(updatedUser.getTitle());
            user.setLocation(updatedUser.getLocation());
            user.setBio(updatedUser.getBio());
            user.setSkills(updatedUser.getSkills());
            user.setGithub(updatedUser.getGithub());
            user.setLinkedin(updatedUser.getLinkedin());

            User savedUser = userService.updateUser(user);
            System.out.println("Updated/Created user: " + savedUser);
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            System.out.println("Failed to update/create user for email: " + email + " - " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }
}