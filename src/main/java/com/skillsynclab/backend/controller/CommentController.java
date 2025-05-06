package com.skillsynclab.backend.controller;

import com.skillsynclab.backend.exception.UnauthorizedException;
import com.skillsynclab.backend.model.Author;
import com.skillsynclab.backend.model.Comment;
import com.skillsynclab.backend.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "http://localhost:8080", allowCredentials = "true")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/post/{postId}")
    public ResponseEntity<Comment> addComment(
            @PathVariable String postId,
            @RequestBody Map<String, String> payload,
            @AuthenticationPrincipal OAuth2User principal) {
        try {
            String content = payload.get("content");
            if (content == null || content.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(null);
            }

            Author author = new Author();
            author.setName(principal.getAttribute("name"));
            author.setAvatar(principal.getAttribute("picture"));

            Comment comment = commentService.addComment(postId, content, author);
            return ResponseEntity.ok(comment);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(null); // Bad request for invalid post or disabled comments
        }
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Comment> updateComment(
            @PathVariable String commentId,
            @RequestBody Map<String, String> payload,
            @AuthenticationPrincipal OAuth2User principal) {
        try {
            String content = payload.get("content");
            if (content == null || content.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(null);
            }

            String userName = principal.getAttribute("name");
            Comment updatedComment = commentService.updateComment(commentId, content, userName);
            return ResponseEntity.ok(updatedComment);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(403).body(null); // Forbidden
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(null); // Not found
        }
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable String commentId,
            @AuthenticationPrincipal OAuth2User principal) {
        try {
            String userName = principal.getAttribute("name");
            commentService.deleteComment(commentId, userName);
            return ResponseEntity.ok().build();
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(403).build(); // Forbidden
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).build(); // Not found
        }
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<Comment>> getCommentsByPost(@PathVariable String postId) {
        try {
            List<Comment> comments = commentService.getCommentsByPost(postId);
            return ResponseEntity.ok(comments);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).build(); // Not found
        }
    }
}

