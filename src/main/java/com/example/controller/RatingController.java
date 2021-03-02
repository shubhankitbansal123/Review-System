package com.example.controller;

import com.example.models.Rating;
import com.example.models.RatingAverage;
import com.example.repository.HotelRepository;
import com.example.repository.RatingRepository;
import com.example.repository.UsersRepository;
import com.example.service.HotelService;
import com.example.service.RatingService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @Autowired
    private UserService userService;

    @Autowired
    private HotelService hotelService;

    @PostMapping("/rateHotel")
    public String rateHotel(@RequestBody Rating rating, @RequestHeader("user_token") String userToken){
        boolean count = userService.checkUser(userToken,rating.getUser_id(),rating.getType());
        if(!count){
            return "Invalid Request";
        }
        if(rating.getType().equalsIgnoreCase("Hotel")) {
            boolean count1 = hotelService.getHotelCount(rating.getType_id());
            if (!count1) {
                return "Invalid request";
            }
        }
        ratingService.save(rating);
        return "rating added successfully";
    }

    @GetMapping("/averageRating")
    public RatingAverage ratingAverage(){
        return ratingService.ratingAverage();
    }
}
