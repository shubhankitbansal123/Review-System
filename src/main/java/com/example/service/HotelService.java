package com.example.service;

import com.example.models.Hotel;
import com.example.models.RatingAverage;
import com.example.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    public void save(Hotel hotel) {
        hotelRepository.save(hotel);
    }

    public boolean getHotelCount(Integer id) {
        return  hotelRepository.getHotelCount(id);
    }

    public void deleteHotel(Integer id) {
        hotelRepository.deleteHotel(id);
    }

}
