package com.skillsynclab.backend.service;

import com.skillsynclab.backend.model.SkillPost;
import com.skillsynclab.backend.repository.SkillPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SkillPostService {
    @Autowired
    private SkillPostRepository skillPostRepository;

    public List<SkillPost> getAllPosts() {
        return skillPostRepository.findAll();
    }

    public Optional<SkillPost> getPostById(String id) {
        return skillPostRepository.findById(id);
    }

    public SkillPost createPost(SkillPost post) {
        post.setCreatedAt(LocalDateTime.now());
        SkillPost savedPost = skillPostRepository.save(post);
        System.out.println("Saved Post in Service: " + savedPost);
        return savedPost;
    }

    public SkillPost updatePost(String id, SkillPost postDetails) {
        SkillPost post = skillPostRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
        post.setTitle(postDetails.getTitle());
        post.setDescription(postDetails.getDescription());
        post.setAuthor(postDetails.getAuthor());
        post.setLikes(postDetails.getLikes());
        post.setComments(postDetails.getComments());
        post.setImage(postDetails.getImage());
        post.setCategory(postDetails.getCategory());
        post.setTags(postDetails.getTags());
        post.setAllowComments(postDetails.isAllowComments());
        post.setVisibility(postDetails.getVisibility());
        return skillPostRepository.save(post);
    }

    public void deletePost(String id) {
        skillPostRepository.deleteById(id);
    }
}