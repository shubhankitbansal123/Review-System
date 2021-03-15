package com.example.service;

import com.example.models.Comment;
import com.example.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public void save(Comment comment) {
        commentRepository.save(comment);
    }

    public boolean checkCommentIdAndUserId(Integer commentId, Integer userId) {
        return commentRepository.checkCommentIdAndUserId(commentId,userId);
    }

    public void updateComment(Integer commentId, String comment) {
        commentRepository.updateComment(commentId,comment);
    }

    public void deleteById(Integer commentId) {
        commentRepository.deleteById(commentId);
    }

    public List<String> getCommentFromUser_id(Integer userId) {
        return commentRepository.getCommentFromUser_id(userId);
    }
}
