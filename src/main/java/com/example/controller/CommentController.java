package com.example.controller;


import com.example.models.Comment;
import com.example.models.Users;
import com.example.service.CommentService;
import com.example.service.HotelService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentController {

    @Autowired
    private final UserService userService;

    @Autowired
    private final HotelService hotelService;

    @Autowired
    private final CommentService commentService;

    public CommentController(UserService userService, HotelService hotelService, CommentService commentService) {
        this.userService = userService;
        this.hotelService = hotelService;
        this.commentService = commentService;
    }

    @PostMapping("/createComment")
    public String createComment(@RequestBody Comment comment,@RequestHeader("user_token") String userToken){
        try {
            Users users = userService.getUserInfo(userToken);
            comment.setUserid(users.getUserid());
            comment.setType(users.getType());
            if (users.getType().equalsIgnoreCase("Hotel")) {
                if (ObjectUtils.isEmpty(comment.getHotelid()) || ObjectUtils.isEmpty(comment.getComment()) || ObjectUtils.isEmpty(comment.getName())) {
                    return "Some fields are empty";
                }
                boolean count = hotelService.getHotelCount(comment.getHotelid());
                if (!count) {
                    return "Invalid request";
                }
                commentService.save(comment);
                return "Comment Added Successfully";

            } else if (users.getType().equalsIgnoreCase("Inventory")) {
                if (ObjectUtils.isEmpty(comment.getInventoryid()) || ObjectUtils.isEmpty(comment.getComment())) {
                    return "Some fields are empty";
                }
                commentService.save(comment);
                return "Comment Added Successfully";
            } else if (users.getType().equalsIgnoreCase("Ott")) {
                if (ObjectUtils.isEmpty(comment.getOttid()) || ObjectUtils.isEmpty(comment.getComment())) {
                    return "Some fields are empty";
                }
                commentService.save(comment);
                return "Comment Added Successfully";
            } else {
                return "Something is wrong";
            }
        }catch (Exception e){
            return "Comment does not added";
        }
    }

    @PutMapping("/editComment")
    public String editComment(@RequestHeader("user_token") String userToken,@RequestParam("comment_id") Integer commentId,@RequestBody String comment){
        try {
            Users users = userService.getUserInfo(userToken);
            if (ObjectUtils.isEmpty(commentId) || ObjectUtils.isEmpty(comment)) {
                return "Some fields are empty";
            }
            boolean check = commentService.checkCommentIdAndUserId(commentId, users.getUserid());
            if (check) {
                commentService.updateComment(commentId, comment);
                return "Comment Successfully updated";
            }
            return "Some Information is wrong";
        }catch (Exception e){
            return "Comment edited unsuccessful";
        }
    }

    @DeleteMapping("/deleteComment")
    private String deleteComment(@RequestHeader("user_token") String userToken,@RequestParam("comment_id") Integer commentId){
        try {
            Users users = userService.getUserInfo(userToken);
            if (ObjectUtils.isEmpty(commentId)) {
                return "Some fields are empty";
            }
            boolean check = commentService.checkCommentIdAndUserId(commentId, users.getUserid());
            if (check) {
                commentService.deleteById(commentId);
                return "Comment Deleted Successfully";
            }
            return "Something is wrong";
        }catch (Exception e){
            return "Comment does not deleted";
        }
    }

    @GetMapping("/getComment")
    private ResponseEntity<Object> getComment(@RequestHeader("user_token") String userToken){
        try {
            Users users = userService.getUserInfo(userToken);
            List<String> comments = commentService.getCommentFromUser_id(users.getUserid());
            return new ResponseEntity<>(comments,HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity<>("Comment does not fetch", HttpStatus.NOT_FOUND);
        }
    }
}
