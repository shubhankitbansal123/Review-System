package com.example.service;

import com.example.models.Rating;
import com.example.models.RatingAverage;
import com.example.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    public void deleteDataOfHotel(Integer id) {
        ratingRepository.deleteDataOfHotel(id);
    }

    public void save(Rating rating) {
        ratingRepository.save(rating);
    }

    public RatingAverage ratingAverage(){
        return ratingRepository.ratingAverage();
    }
}
