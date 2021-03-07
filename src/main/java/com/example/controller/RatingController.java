package com.example.controller;

import com.example.models.*;
import com.example.service.HotelService;
import com.example.service.RatingService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        Users users = userService.getUserInfo(userToken);
        rating.setUser_id(users.getUser_id());
        rating.setType(users.getType());
        if(users.getType().equalsIgnoreCase("Hotel")){
            if(StringUtils.isEmpty(rating.getType_id()) || StringUtils.isEmpty(rating.getRateHotel())) {
                return "Some fields are empty";
            }
            boolean count = hotelService.getHotelCount(rating.getType_id());
            if (!count) {
                return "Invalid request";
            }
            rating.setRatingOtt(new RatingOtt(0,0,0,0,0));
            rating.setRatingInventory(new RatingInventory(0,0,0,0,0));
            ratingService.save(rating);
            return "rating added successfully";
        }
        else if(users.getType().equalsIgnoreCase("Inventory")) {
            if(StringUtils.isEmpty(rating.getType_id()) || StringUtils.isEmpty(rating.getRatingInventory())) {
                return "Some fields are empty";
            }
            rating.setRateHotel(new RatingHotel(0,0,0,0,0));
            rating.setRatingOtt(new RatingOtt(0,0,0,0,0));
            ratingService.save(rating);
            return "rating added successfully";
        }
        else if(users.getType().equalsIgnoreCase("Ott")) {
            if(StringUtils.isEmpty(rating.getType_id()) || StringUtils.isEmpty(rating.getRatingOtt())) {
                return "Some fields are empty";
            }
            rating.setRateHotel(new RatingHotel(0,0,0,0,0));
            rating.setRatingInventory(new RatingInventory(0,0,0,0,0));
            ratingService.save(rating);
            return "rating added successfully";
        }
        else {
            return "Something is wrong";
        }
    }

    @GetMapping("/averageRating")
    public List<RatingAverage> ratingAverage(){
        return ratingService.ratingAverage();
    }
}
