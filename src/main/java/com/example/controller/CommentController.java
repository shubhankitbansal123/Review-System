package com.example.controller;


import com.example.Exception.*;
import com.example.models.Comment;
import com.example.models.Users;
import com.example.service.CommentService;
import com.example.service.HotelService;
import com.example.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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

    Logger logger = LoggerFactory.getLogger(CommentController.class);

    public CommentController(UserService userService, HotelService hotelService, CommentService commentService) {
        this.userService = userService;
        this.hotelService = hotelService;
        this.commentService = commentService;
    }

    @PostMapping("/createComment")
    public ResponseEntity<Object> createComment(@RequestBody(required = false) Comment comment,@RequestHeader(name = "user_token",required = false) String userToken) throws GenericException {
        if (ObjectUtils.isEmpty(comment)) {
            throw new ParameterMissingException("Comment body is missing");
        }
        Users users = userService.getUserInfo(userToken);
        comment.setUserid(users.getUserid());
        if(ObjectUtils.isEmpty(comment.getType())){
            logger.error("User Type need not to be null");
            throw new DataNotFoundException("User Type need not to be null");
        }
        if(!comment.getType().equalsIgnoreCase(users.getType())){
            logger.error("User does not belong to " + comment.getType());
            throw new WrongInformationException("User does not belong to " + comment.getType());
        }
        comment.setType(users.getType());
        if (users.getType().equalsIgnoreCase("Hotel")) {
            logger.info("User is of type Hotel");
            if (ObjectUtils.isEmpty(comment.getTypeid()) || ObjectUtils.isEmpty(comment.getComment()) || ObjectUtils.isEmpty(comment.getName())) {
                logger.error("Some fields are empty");
                throw new DataNotFoundException("Some fields are empty");
            }
            boolean count = hotelService.getHotelCountWithName(comment.getTypeid(),comment.getName());
            if (!count) {
                logger.error("Hotel does not exist for given information");
                throw new WrongInformationException("Hotel information is not valid");
            }
            try {
                commentService.save(comment);
            }catch (DataIntegrityViolationException e){
                throw new DuplicateEntryException("Duplicate Entry is not allowed");
            }
            logger.info("Create comment successful");
            return new ResponseEntity<>("Comment Added Successfully",HttpStatus.OK);

        } else if (users.getType().equalsIgnoreCase("Inventory")) {
            logger.info("User is of type Inventory");
            if (ObjectUtils.isEmpty(comment.getTypeid()) || ObjectUtils.isEmpty(comment.getComment())) {
                logger.error("Some fields are empty");
                throw new DataNotFoundException("Some fields are empty");
            }
            try {
                commentService.save(comment);
            }catch (DataIntegrityViolationException e){
                throw new DuplicateEntryException("Duplicate entry is not allowed");
            }
            logger.info("Create comment successful");
            return new ResponseEntity<>("Comment Added Successfully",HttpStatus.OK);
        } else if (users.getType().equalsIgnoreCase("Ott")) {
            logger.info("User is of type Ott");
            if (ObjectUtils.isEmpty(comment.getTypeid()) || ObjectUtils.isEmpty(comment.getComment())) {
                logger.error("Some fields are empty");
                throw new DataNotFoundException("Some fields are empty");
            }
            try {
                commentService.save(comment);
            }catch (DataIntegrityViolationException e){
                throw new DuplicateEntryException("Duplicate Entry is not allowed");
            }
            logger.info("Create comment successful");
            return new ResponseEntity<>("Comment Added Successfully",HttpStatus.OK);
        } else {
            logger.error("Something is wrong");
            throw new GenericException("Something is wrong");
        }
    }

    @PutMapping("/editComment")
    public ResponseEntity<Object> editComment(@RequestHeader(name = "user_token",required = false) String userToken,@RequestParam(name = "comment_id",required = false) Integer commentId,@RequestBody(required = false) String comment) {
        if(ObjectUtils.isEmpty(commentId) || ObjectUtils.isEmpty(comment)){
            throw new ParameterMissingException("Comment or comment id is missing");
        }
        Users users = userService.getUserInfo(userToken);
        if (ObjectUtils.isEmpty(commentId) || ObjectUtils.isEmpty(comment)) {
            logger.error("Some fields are missing");
            throw new DataNotFoundException("Some fields are empty");
        }
        boolean check = commentService.checkCommentIdAndUserId(commentId, users.getUserid());
        if (check) {
            commentService.updateComment(commentId, comment);
            logger.info("Comment Edited Successful");
            return new ResponseEntity<>("Comment Added Updated",HttpStatus.OK);
        }
        logger.error("User is not authorized");
        throw new WrongInformationException("User is not Authorized");
    }

    @DeleteMapping("/deleteComment")
    private ResponseEntity<Object> deleteComment(@RequestHeader(name = "user_token",required = false) String userToken,@RequestParam(name = "comment_id",required = false) Integer commentId) {
        if(ObjectUtils.isEmpty(commentId)){
            throw new ParameterMissingException("Comment id is missing");
        }
        Users users = userService.getUserInfo(userToken);
        if (ObjectUtils.isEmpty(commentId)) {
            throw new DataNotFoundException("Some fields are empty");
        }
        boolean check = commentService.checkCommentIdAndUserId(commentId, users.getUserid());
        if (check) {
            commentService.deleteById(commentId);
            return new ResponseEntity<>("Comment Deleted Successfully",HttpStatus.OK);
        }
        throw new WrongInformationException("User is not Authorized");
    }

    @GetMapping("/getComment")
    private ResponseEntity<Object> getComment(@RequestHeader(name = "user_token",required = false) String userToken) throws GenericException {
        Users users = userService.getUserInfo(userToken);
        List<String> comments = commentService.getCommentFromUser_id(users.getUserid());
        if(comments==null){
            logger.error("No comments for this user");
            throw new DataNotFoundException("No comments for this user");
        }
        try{
            logger.info("Comment fetch successful");
            return new ResponseEntity<>(comments,HttpStatus.OK);
        }catch (Exception e){
            logger.error("Comment does not fetch");
            throw new GenericException("Comment does not fetch");
        }
    }

    @GetMapping("/getCommentForSpecificService")
    private ResponseEntity<Object> getCommentForSpecificService(@RequestParam(name = "type",required = false) String type,@RequestParam(name = "type_id",required = false) Integer typeId) throws GenericException {
        if(ObjectUtils.isEmpty(type) || ObjectUtils.isEmpty(typeId)){
            throw new ParameterMissingException("Type or TypeId is missing");
        }
        boolean count1=commentService.checkCommentForSpecificService(type,typeId);
        if(!count1){
            logger.error("No comment for " + type + " with type id " + typeId);
            throw new DataNotFoundException("No comments for " + type + " with type id " + typeId);
        }
        List<String> comments = commentService.getCommentForSpecificService(type,typeId);
        logger.info("Comment fetch successful");
        return new ResponseEntity<>(comments,HttpStatus.OK);

    }
}
