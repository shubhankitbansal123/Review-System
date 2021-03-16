package com.example.controller;

import com.example.models.*;
import com.example.service.CommentService;
import com.example.service.HotelService;
import com.example.service.RatingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class HotelController {

    @Autowired
    private final HotelService hotelService;

    @Autowired
    private final RatingService ratingService;

    @Autowired
    private final CommentService commentService;

    public HotelController(HotelService hotelService, RatingService ratingService, CommentService commentService) {
        this.hotelService = hotelService;
        this.ratingService = ratingService;
        this.commentService = commentService;
    }

    @PostMapping("/registerHotel")
    public String registerHotel(@RequestBody Hotel hotel){
        try {
            if (ObjectUtils.isEmpty(hotel.getHotelname()) || ObjectUtils.isEmpty(hotel.getContact()) || ObjectUtils.isEmpty(hotel.getLocation())) {
                return "Information is missing";
            }
            RatingHotel ratingHotel = new RatingHotel(0.0,0.0,0.0,0.0,0.0);
            ObjectMapper objectMapper = new ObjectMapper();
            hotel.setRating(objectMapper.convertValue(ratingHotel,Map.class));
            hotelService.save(hotel);
            return "New Hotel registered";
        }catch (Exception e){
            return "Hotel is not registered";
        }
    }

    @DeleteMapping("/deleteHotel")
    public String deleteHotel(@RequestHeader("user_token") String userToken,@RequestParam("hotel_id") Integer id){
        try {
            boolean count1 = hotelService.getHotelCount(id);
            if (!count1) {
                return "Hotel does not exist";
            }
            hotelService.deleteHotel(id);
            return "Hotel deleted";
        }catch (Exception e){
            return "Hotel data is not deleted";
        }


    }

    @GetMapping("/fetchHotelByNameAndLocation")
    public ResponseEntity<Object> fetchHotelByNameAndLocation(@RequestParam("nameorlocation") String nameOrLocation,@RequestParam("value") String value){
        try {
            System.out.println(nameOrLocation+" " +value);
            return new ResponseEntity<>(hotelService.fetchHotelByNameAndLocation(nameOrLocation, value),HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity<>( "Hotel does not exist", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/fetchHotelRecordsInPagination")
    public ResponseEntity<Object> fetchHotelRecordsInPagination(@RequestParam("pageno") Integer pageno, @RequestParam("pagesize") Integer pagesize){
        try {
            return new ResponseEntity<>(hotelService.fetchHotelRecordsInPagination(pageno,pagesize,"averagerating"),HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity<>("Data not found",HttpStatus.NOT_FOUND);
        }
    }
}
