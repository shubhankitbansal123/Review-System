package com.example.service;

import com.example.models.Hotel;
import com.example.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HotelService {

    @Autowired
    private final HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }


    public void save(Hotel hotel) {
        hotelRepository.save(hotel);
    }

    public boolean getHotelCount(Integer id) {
        return  hotelRepository.getHotelCount(id);
    }

    public void deleteHotel(Integer id) {
        hotelRepository.deleteHotel(id);
    }

    public Hotel fetchHotelByNameAndLocation(String hotelName, String location) {
        return hotelRepository.fetchHotelByNameAndLocation(hotelName,location);
    }
}
