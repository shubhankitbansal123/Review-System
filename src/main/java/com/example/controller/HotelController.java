package com.example.controller;

import com.example.models.*;
import com.example.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

@RestController
public class HotelController {

    @Autowired
    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @PostMapping("/registerHotel")
    public String registerHotel(@RequestBody Hotel hotel){
        try {
            if (ObjectUtils.isEmpty(hotel.getHotelname()) || ObjectUtils.isEmpty(hotel.getContact()) || ObjectUtils.isEmpty(hotel.getLocation())) {
                return "Information is missing";
            }
            hotelService.save(hotel);
            return "New Hotel registered";
        }catch (Exception e){
            return "Hotel is not registered";
        }
    }

    @DeleteMapping("/deleteHotel")  // work fine but the problem is it gives an error no result were returned by the query
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
    public ResponseEntity<Object> fetchHotelByNameAndLocation(@RequestParam("hotel_name") String hotelName,@RequestParam("location") String location){
        try {
            return new ResponseEntity<>(hotelService.fetchHotelByNameAndLocation(hotelName, location),HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity<>( "Hotel does not exist", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/fetchHotelRecordsInPagination")
    public ResponseEntity<Object> fetchHotelRecordsInPagination(@RequestParam("pageno") Integer pageno, @RequestParam("pagesize") Integer pagesize){
        try {
            return new ResponseEntity<>(hotelService.fetchHotelRecordsInPagination(pageno,pagesize,"averagerating"),HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity<>("",HttpStatus.NOT_FOUND);
        }
    }
}
