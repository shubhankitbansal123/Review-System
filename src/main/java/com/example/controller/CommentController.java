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
    public ResponseEntity<Object> createComment(@RequestBody Comment comment,@RequestHeader("user_token") String userToken){
        try {
            Users users = userService.getUserInfo(userToken);
            comment.setUserid(users.getUserid());
            if(ObjectUtils.isEmpty(comment.getType())){
                return new ResponseEntity<>("Some fields are empty",HttpStatus.NOT_FOUND);
            }
            if(!comment.getType().equalsIgnoreCase(users.getType())){
                return new ResponseEntity<>("User does not belong to " + comment.getType(),HttpStatus.BAD_REQUEST);
            }
            comment.setType(users.getType());
            if (users.getType().equalsIgnoreCase("Hotel")) {
                if (ObjectUtils.isEmpty(comment.getTypeid()) || ObjectUtils.isEmpty(comment.getComment()) || ObjectUtils.isEmpty(comment.getName())) {
                    return new ResponseEntity<>("Some fields are empty",HttpStatus.NOT_FOUND);
                }
                boolean count = hotelService.getHotelCountWithName(comment.getTypeid(),comment.getName());
                if (!count) {
                    return new ResponseEntity<>("Invalid Data",HttpStatus.BAD_REQUEST);
                }
                commentService.save(comment);
                return new ResponseEntity<>("Comment Added Successfully",HttpStatus.ACCEPTED);

            } else if (users.getType().equalsIgnoreCase("Inventory")) {
                if (ObjectUtils.isEmpty(comment.getTypeid()) || ObjectUtils.isEmpty(comment.getComment())) {
                    return new ResponseEntity<>("Some fields are empty",HttpStatus.NOT_FOUND);
                }
                commentService.save(comment);
                return new ResponseEntity<>("Comment Added Successfully",HttpStatus.ACCEPTED);
            } else if (users.getType().equalsIgnoreCase("Ott")) {
                if (ObjectUtils.isEmpty(comment.getTypeid()) || ObjectUtils.isEmpty(comment.getComment())) {
                    return new ResponseEntity<>("Some fields are empty",HttpStatus.NOT_FOUND);
                }
                commentService.save(comment);
                return new ResponseEntity<>("Comment Added Successfully",HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>("Something is wrong",HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            return new ResponseEntity<>("Comment does not added",HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/editComment")
    public ResponseEntity<Object> editComment(@RequestHeader("user_token") String userToken,@RequestParam("comment_id") Integer commentId,@RequestBody String comment){
        try {
            Users users = userService.getUserInfo(userToken);
            if (ObjectUtils.isEmpty(commentId) || ObjectUtils.isEmpty(comment)) {
                return new ResponseEntity<>("Some fields are empty",HttpStatus.NOT_FOUND);
            }
            boolean check = commentService.checkCommentIdAndUserId(commentId, users.getUserid());
            if (check) {
                commentService.updateComment(commentId, comment);
                return new ResponseEntity<>("Comment Added Updated",HttpStatus.ACCEPTED);
            }
            return new ResponseEntity<>("User is not Authorized",HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return new ResponseEntity<>("Comment Edited Unsuccessful",HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteComment")
    private ResponseEntity<Object> deleteComment(@RequestHeader("user_token") String userToken,@RequestParam("comment_id") Integer commentId){
        try {
            Users users = userService.getUserInfo(userToken);
            if (ObjectUtils.isEmpty(commentId)) {
                return new ResponseEntity<>("Some fields are empty",HttpStatus.NOT_FOUND);
            }
            boolean check = commentService.checkCommentIdAndUserId(commentId, users.getUserid());
            if (check) {
                commentService.deleteById(commentId);
                return new ResponseEntity<>("Comment Deleted Successfully",HttpStatus.ACCEPTED);
            }
            return new ResponseEntity<>("User is not Authorized",HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return new ResponseEntity<>("Comment does not deleted",HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getComment")
    private ResponseEntity<Object> getComment(@RequestHeader("user_token") String userToken){
        try {
            Users users = userService.getUserInfo(userToken);
            List<String> comments = commentService.getCommentFromUser_id(users.getUserid());
            if(comments==null){
                return new ResponseEntity<>("No comments for this user",HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(comments,HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity<>("Comment does not fetch", HttpStatus.BAD_REQUEST);
        }
    }

    private ResponseEntity<Object> getCommentForSpecificService(@RequestParam("type") String type,@RequestParam("type_id") String typeId){
        try {
            List<String> comments = commentService.getCommentForSpecificService(type,typeId);
            if(comments==null){
                return new ResponseEntity<>("No comments for " + type + "with type id " + typeId,HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(comments,HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity<>("Comment does not fetch", HttpStatus.BAD_REQUEST);
        }
    }
}
