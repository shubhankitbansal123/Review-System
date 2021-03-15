package com.example.service;

import com.example.models.Hotel;
import com.example.models.RatingHotel;
import com.example.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

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
        hotelRepository.deleteById(id);
    }

    public Hotel fetchHotelByNameAndLocation(String hotelName, String location) {
        return hotelRepository.fetchHotelByNameAndLocation(hotelName,location);
    }

    public void updateAverageRating(RatingHotel ratingHotel,float average, Integer hotelid) {
        hotelRepository.updateAverageRating(hotelid,average,ratingHotel.getFood(),ratingHotel.getService(),ratingHotel.getLocality(),ratingHotel.getHygine(),ratingHotel.getSecurity());
    }

    public Hotel fetchHotel(Integer typeid) {
        return hotelRepository.fetchHotel(typeid);
    }

    public List<Hotel> fetchHotelRecordsInPagination(Integer pageno, Integer pagesize, String sortby) {
        Pageable pageable = PageRequest.of(pageno,pagesize,Sort.by(sortby).descending());
        return hotelRepository.findAll(pageable).getContent();
    }
}
