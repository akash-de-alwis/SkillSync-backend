package com.skillsynclab.backend.service;


import com.skillsynclab.backend.exception.UnauthorizedException;
import com.skillsynclab.backend.model.Author;
import com.skillsynclab.backend.model.Comment;
import com.skillsynclab.backend.model.SkillPost;
import com.skillsynclab.backend.repository.CommentRepository;
import com.skillsynclab.backend.repository.SkillPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private SkillPostRepository skillPostRepository;

    public Comment addComment(String postId, String content, Author author) {
        SkillPost post = skillPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        if (!post.isAllowComments()) {
            throw new RuntimeException("Comments are disabled for this post");
        }
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setAuthor(author);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setPost(post);
        return commentRepository.save(comment);
    }

    public Comment updateComment(String commentId, String content, String userName) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        if (!comment.getAuthor().getName().equals(userName)) {
            throw new UnauthorizedException("You are not authorized to edit this comment");
        }
        comment.setContent(content);
        comment.setCreatedAt(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    public void deleteComment(String commentId, String userName) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        if (!comment.getAuthor().getName().equals(userName)) {
            throw new UnauthorizedException("You are not authorized to delete this comment");
        }
        commentRepository.delete(comment);
    }

    public List<Comment> getCommentsByPost(String postId) {
        SkillPost post = skillPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        return commentRepository.findByPost(post);
    }
}

