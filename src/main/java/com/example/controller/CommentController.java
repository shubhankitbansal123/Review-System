package com.example.controller;


import com.example.models.Comment;
import com.example.repository.CommentRepository;
import com.example.repository.HotelRepository;
import com.example.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentController {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private CommentRepository commentRepository;

    @PostMapping("/createComment")
    public String createComment(@RequestBody Comment comment,@RequestHeader("access_token") String accessToken){
        Integer count = usersRepository.checkUser(accessToken,comment.getUser_id());
        Integer count1 = hotelRepository.getHotelCount(comment.getHotel_id());
        if(count==0 || count1==0){
            return "Invalid request";
        }
        commentRepository.save(comment);
        return "Comment Added Successfully";
    }

    @PutMapping("/editComment")
    public String editComment(@RequestHeader("access_token") String accessToken,@RequestBody Comment comment){
        Integer count = usersRepository.checkUser(accessToken,comment.getUser_id());
        Integer count1 = hotelRepository.getHotelCount(comment.getHotel_id());
        if(count==0 || count1==0){
            return "Invalid request";
        }
        commentRepository.save(comment);
        return "Comment Edited Successfully";
    }

    @DeleteMapping("/deleteComment/{id}")
    private String deleteComment(@RequestHeader("access_token") String accessToken,@PathVariable Integer id){
        commentRepository.deleteById(id);
        return "deleted Successfully";
    }

    @GetMapping("/getComment")
    private List<String> getComment(@RequestHeader("access_token") String accessToken){
        Integer userId = usersRepository.getUserIdFromAccessToken(accessToken);
        List<String> comments = commentRepository.getCommentFromUser_id(userId);
        return comments;
    }
}
