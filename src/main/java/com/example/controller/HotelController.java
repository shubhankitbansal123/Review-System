package com.example.controller;

import com.example.models.Hotel;
import com.example.models.Rating;
import com.example.repository.CommentRepository;
import com.example.repository.HotelRepository;
import com.example.repository.RatingRepository;
import com.example.repository.UsersRepository;
import com.example.service.CommentService;
import com.example.service.HotelService;
import com.example.service.RatingService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @Autowired
    private UserService userService;

    @Autowired
    private RatingService ratingService;

    @Autowired
    private CommentService commentService;

    @PostMapping("/registerHotel")
    public String registerHotel(@RequestBody Hotel hotel){
        if(StringUtils.isEmpty(hotel.getHotel_name()) || StringUtils.isEmpty(hotel.getContact()) || StringUtils.isEmpty(hotel.getLocation())){
            return "Information is missing";
        }
        hotelService.save(hotel);
        return "New Hotel registered";
    }

    @DeleteMapping("/deleteHotel")  // work fine but the problem is it gives an error no result were returned by the query
    public String deleteHotel(@RequestHeader("user_token") String userToken,@RequestParam("hotel_id") Integer id){
        boolean count1 = hotelService.getHotelCount(id);
        if(!count1){
            return "Hotel does not exist";
        }
        hotelService.deleteHotel(id);
        ratingService.deleteDataOfHotel(id);
        commentService.deleteDataOfHotel(id);
        return "Hotel deleted";
    }



}
