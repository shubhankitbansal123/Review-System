package com.example.controller;

import com.example.Exception.DataNotFoundException;
import com.example.Exception.GenericException;
import com.example.Exception.ParameterMissingException;
import com.example.models.*;
import com.example.service.CommentService;
import com.example.service.HotelService;
import com.example.service.RatingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.istack.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class HotelController {

    @Autowired
    private final HotelService hotelService;

    @Autowired
    private final RatingService ratingService;

    @Autowired
    private final CommentService commentService;

    Logger logger = LoggerFactory.getLogger(HotelController.class);

    public HotelController(HotelService hotelService, RatingService ratingService, CommentService commentService) {
        this.hotelService = hotelService;
        this.ratingService = ratingService;
        this.commentService = commentService;
    }

    @PostMapping("/registerHotel")
    public ResponseEntity<Object> registerHotel(@RequestBody(required = false) Hotel hotel) throws GenericException {
        if(ObjectUtils.isEmpty(hotel)){
            throw new ParameterMissingException("Hotel body is missing");
        }
        if (ObjectUtils.isEmpty(hotel.getHotelname()) || ObjectUtils.isEmpty(hotel.getContact()) || ObjectUtils.isEmpty(hotel.getLocation())) {
            logger.error("Some information is missing");
            throw new DataNotFoundException("Hotel information is missing");
        }
        try {
            RatingHotel ratingHotel = new RatingHotel(0.0,0.0,0.0,0.0,0.0);
            ObjectMapper objectMapper = new ObjectMapper();
            hotel.setRating(objectMapper.convertValue(ratingHotel,Map.class));
            hotelService.save(hotel);
            logger.info("Hotel Registration Successful");
            return new ResponseEntity<>("New Hotel registered",HttpStatus.OK);
        }catch (Exception e){
            logger.error("Hotel Registration Unsuccessful");
            throw new GenericException("Hotel is not registered");
        }
    }

    @DeleteMapping("/deleteHotel")
    public String deleteHotel(@RequestHeader(name = "user_token",required = false) String userToken,@RequestParam(name = "hotel_id",required = false) Integer id) throws GenericException {
        if(ObjectUtils.isEmpty(id)){
            throw new ParameterMissingException("Hotel id is missing");
        }
        boolean count1 = hotelService.getHotelCount(id);
        if (!count1) {
            logger.error("Hotel does not exist for given id");
            throw new DataNotFoundException("Hotel does not exist");
        }
        try {
            logger.info("Hotel exists for given id");
            hotelService.deleteHotel(id);
            if(ratingService.checkByHotelTypeId(id)){
                ratingService.deleteByHotelTypeId(id);
            }
            if(commentService.checkByHotelTypeId(id)){
                commentService.deleteByHotelTypeId(id);
            }
            logger.info("Hotel delete successful");
            return "Hotel deleted";
        }catch (Exception e){
            logger.error("Hotel delete unsuccessful");
            throw new GenericException("Hotel data is not deleted");
        }
    }

    @GetMapping("/fetchHotelByNameAndLocation")
    public ResponseEntity<Object> fetchHotelByNameAndLocation(@RequestParam(name = "key",required = false) String key,@RequestParam(name = "value",required = false) String value){
        if(ObjectUtils.isEmpty(key) || ObjectUtils.isEmpty(value)){
            throw new ParameterMissingException("Key or value are missing");
        }
        try {
            List<Hotel> hotels = hotelService.fetchHotelByNameAndLocation(key,value);
            logger.info("Hotel information fetched for name and location");
            return new ResponseEntity<>(hotels,HttpStatus.OK);
        }catch (Exception e){
            logger.error("Hotel fetch Unsuccessful");
            throw new DataNotFoundException( "Hotel does not exist");
        }
    }

    @GetMapping("/fetchHotelRecordsInPagination")
    public ResponseEntity<Object> fetchHotelRecordsInPagination(@RequestParam(name = "pageno",required = false,defaultValue = "0") Integer pageno, @RequestParam(name = "pagesize",required = false,defaultValue = "1") Integer pagesize){
        try {
            List<Hotel> hotels = hotelService.fetchHotelRecordsInPagination(pageno,pagesize,"averagerating");
            logger.info("Hotel information fetched in pagination format");
            return new ResponseEntity<>(hotels,HttpStatus.OK);
        }catch (Exception e){
            logger.error("Hotel fetch unsuccessful");
            throw new DataNotFoundException("Data not found");
        }
    }
}
