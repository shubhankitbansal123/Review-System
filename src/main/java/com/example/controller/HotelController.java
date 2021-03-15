package com.example.controller;

import com.example.models.*;
import com.example.service.CommentService;
import com.example.service.HotelService;
import com.example.service.RatingService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        if(StringUtils.isEmpty(hotel.getHotelname()) || StringUtils.isEmpty(hotel.getContact()) || StringUtils.isEmpty(hotel.getLocation())){
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
        return "Hotel deleted";


    }

    @GetMapping("/fetchHotelByNameAndLocation")
    public Hotel fetchHotelByNameAndLocation(@RequestParam("hotel_name") String hotelName,@RequestParam("location") String location){
        return hotelService.fetchHotelByNameAndLocation(hotelName,location);
    }

    @GetMapping("/fetchHotelRecordsInPagination")
    public List<Hotel> fetchHotelRecordsInPagination(@RequestParam("pageno") Integer pageno, @RequestParam("pagesize") Integer pagesize){
        return hotelService.fetchHotelRecordsInPagination(pageno,pagesize,"averagerating");
    }
}
