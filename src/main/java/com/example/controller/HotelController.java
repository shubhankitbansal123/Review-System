package com.example.controller;

import com.example.models.Hotel;
import com.example.repository.HotelRepository;
import com.example.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
public class HotelController {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private UsersRepository usersRepository;

    @PostMapping("/registerHotel")
    public String registerHotel(@RequestBody Hotel hotel){
        if(StringUtils.isEmpty(hotel.getHotel_name()) || StringUtils.isEmpty(hotel.getContact()) || StringUtils.isEmpty(hotel.getLocation())){
            return "Information is missing";
        }
        hotelRepository.save(hotel);
        return "New Hotel registered";
    }

    @DeleteMapping("/deleteHotel/{id}")  // work fine but the problem is it gives an error no result were returned by the query
    public String deleteHotel(@RequestHeader("access_token") String accessToken,@PathVariable Integer id){
        Integer count1 = hotelRepository.getHotelCount(id);
        if(count1==0){
            return "Hotel does not exist";
        }
        hotelRepository.deleteHotel(id);
        return null;
    }



}
