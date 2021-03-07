package com.example.controller;


import com.example.models.Comment;
import com.example.models.Users;
import com.example.repository.CommentRepository;
import com.example.repository.HotelRepository;
import com.example.repository.UsersRepository;
import com.example.service.CommentService;
import com.example.service.HotelService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
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
        Users users = userService.getUserInfo(userToken);
        comment.setUser_id(users.getUser_id());
        comment.setType(users.getType());
        if(users.getType().equalsIgnoreCase("Hotel")) {
            if(StringUtils.isEmpty(comment.getType_id()) || StringUtils.isEmpty(comment.getComment())){
                return "Some fields are empty";
            }
            boolean count = hotelService.getHotelCount(comment.getType_id());
            if (!count) {
                return "Invalid request";
            }
            commentService.save(comment);
            return "Comment Added Successfully";
        }
        else if(users.getType().equalsIgnoreCase("Inventory")){
            if(StringUtils.isEmpty(comment.getType_id()) || StringUtils.isEmpty(comment.getComment())){
                return "Some fields are empty";
            }
            commentService.save(comment);
            return "Comment Added Successfully";
        }
        else if(users.getType().equalsIgnoreCase("Ott")){
            if(StringUtils.isEmpty(comment.getType_id()) || StringUtils.isEmpty(comment.getComment())){
                return "Some fields are empty";
            }
            commentService.save(comment);
            return "Comment Added Successfully";
        }
        else {
            return "Something is wrong";
        }
    }

    @PutMapping("/editComment")
    public String editComment(@RequestHeader("user_token") String userToken,@RequestParam("comment_id") Integer commentId,@RequestBody String comment){
        Users users = userService.getUserInfo(userToken);
        if(StringUtils.isEmpty(commentId) || StringUtils.isEmpty(comment)){
            return "Some fields are empty";
        }
        boolean check = commentService.checkCommentIdAndUserId(commentId,users.getUser_id());
        if(check){
            commentService.updateComment(commentId,comment);
            return "Comment Successfully updated";
        }
        return "Some Information is wrong";
    }

    @DeleteMapping("/deleteComment")
    private String deleteComment(@RequestHeader("user_token") String userToken,@RequestParam("comment_id") Integer commentId){
        Users users = userService.getUserInfo(userToken);
        if(StringUtils.isEmpty(commentId)){
            return "Some fields are empty";
        }
        boolean check = commentService.checkCommentIdAndUserId(commentId,users.getUser_id());
        if(check) {
            commentService.deleteById(commentId);
            return "Comment Deleted Successfully";
        }
        return "Something is wrong";
    }

    @GetMapping("/getComment")
    private List<String> getComment(@RequestHeader("user_token") String userToken){
        Users users = userService.getUserInfo(userToken);
        List<String> comments = commentService.getCommentFromUser_id(users.getUser_id());
        return comments;
    }
}
