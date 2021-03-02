package com.example.controller;


import com.example.models.Comment;
import com.example.repository.CommentRepository;
import com.example.repository.HotelRepository;
import com.example.repository.UsersRepository;
import com.example.service.CommentService;
import com.example.service.HotelService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentController {

    @Autowired
    private UserService userService;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private CommentService commentService;

    @PostMapping("/createComment")
    public String createComment(@RequestBody Comment comment,@RequestHeader("user_token") String userToken){
        if(comment.getType().equalsIgnoreCase("Hotel")) {
            boolean count1 = hotelService.getHotelCount(comment.getType_id());
            if (!count1) {
                return "Invalid request";
            }
        }
        comment.setUser_id(userService.getUserIdFromUserToken(userToken));
        commentService.save(comment);
        return "Comment Added Successfully";
    }

    @PutMapping("/editComment")
    public String editComment(@RequestHeader("user_token") String userToken,@RequestParam("user_id") Integer userId,@RequestParam("comment_id") Integer commentId,@RequestBody String comment){
        boolean check = commentService.checkCommentIdAndUserId(commentId,userId);
        if(check){
            commentService.updateComment(commentId,comment);
            return "Comment Successfully updated";
        }
        return "Some Information is wrong";
    }

    @DeleteMapping("/deleteComment")
    private String deleteComment(@RequestHeader("user_token") String userToken,@RequestParam("comment_id") Integer commentId,@RequestParam("user_id") Integer userId){
        boolean check = commentService.checkCommentIdAndUserId(commentId,userId);
        if(check) {
            commentService.deleteById(commentId);
            return "Comment Deleted Successfully";
        }
        return "Something is wrong";
    }

    @GetMapping("/getComment")
    private List<String> getComment(@RequestHeader("user_token") String userToken,@RequestParam("user_id") Integer userId){
        List<String> comments = commentService.getCommentFromUser_id(userId);
        return comments;
    }
}
