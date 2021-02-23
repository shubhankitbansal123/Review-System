package com.example.controller;

import com.example.models.Rating;
import com.example.repository.HotelRepository;
import com.example.repository.RatingRepository;
import com.example.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RatingController {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @PostMapping("/rateHotel")
    public String rateHotel(@RequestBody Rating rating, @RequestHeader("access_token") String accessToken){
        Integer count = usersRepository.checkUser(accessToken,rating.getUser_id());
        Integer count1 = hotelRepository.getHotelCount(rating.getHotel_id());
        if(count==0 || count1==0){
            return "Invalid request";
        }
        ratingRepository.save(rating);
        return "rating added successfully";
    }
}
