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
import java.util.Map;

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

    public List<Hotel> fetchHotelByNameAndLocation(String nameOrLocation, String value) {
        String value1="%";
        for(int i=0;i<value.length();i++){
            value1+=value.charAt(i)+"%";
        }
        System.out.println(value1);
        if(nameOrLocation.equalsIgnoreCase("name")){
            List<Hotel> hotels =  hotelRepository.fetchHotelByNameRegex(value1);
            System.out.println(hotels.toString());
            return hotels;
        }
        else{
            List<Hotel> hotels = hotelRepository.fetchHotelByLocationRegex(value1);
            System.out.println(hotels.toString());
            return hotels;
        }
    }

    public void updateAverageRating(Map<String,Double> ratingHotel, Double average, Integer hotelid) {
        hotelRepository.updateAverageRating(hotelid,average,ratingHotel.get("food"),ratingHotel.get("service"),ratingHotel.get("locality"),ratingHotel.get("hygine"),ratingHotel.get("security"));
    }

    public Hotel fetchHotel(Integer typeid,String name) {
        return hotelRepository.fetchHotel(typeid,name);
    }

    public List<Hotel> fetchHotelRecordsInPagination(Integer pageno, Integer pagesize, String sortby) {
        Pageable pageable = PageRequest.of(pageno,pagesize,Sort.by(sortby).descending());
        return hotelRepository.findAll(pageable).getContent();
    }

    public boolean getHotelCountWithName(Integer typeid, String name) {
        return  hotelRepository.getHotelCountWithName(typeid,name);
    }
}
