package com.example.service;

import com.example.models.AverageRating;
import com.example.models.RatingInventory;
import com.example.models.RatingOtt;
import com.example.repository.AverageRatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AverageRatingService {

    @Autowired
    private AverageRatingRepository averageRatingRepository;

    public void updateRatingInventory(RatingInventory ratingInventory,float average, Integer inventoryid) {
        averageRatingRepository.updateRatingInventory(inventoryid,average,ratingInventory.getA(),ratingInventory.getB(),ratingInventory.getC(),ratingInventory.getD(),ratingInventory.getE());
    }

    public void updateRatingOtt(RatingOtt ratingOtt, float average, Integer ottid) {
        averageRatingRepository.updateRatingOtt(ottid,average,ratingOtt.getA(),ratingOtt.getB(),ratingOtt.getC(),ratingOtt.getD(),ratingOtt.getE());
    }
    public AverageRating getAverageRatingForInventory(Integer inventoryid) {
        return averageRatingRepository.getAverageRatingForInventory(inventoryid);
    }

    public AverageRating getAverageRatingForOtt(Integer ottid) {
        return averageRatingRepository.getAverageRatingForOtt(ottid);
    }
    public void save(AverageRating averageRating) {
        averageRatingRepository.save(averageRating);
    }

    public List<AverageRating> findInventoryRecordInPagination(Integer pageno, Integer pagesize, String averagerate) {
        Pageable pageable = PageRequest.of(pageno,pagesize, Sort.by(averagerate));
        return averageRatingRepository.findAllByType("Inventory",pageable).getContent();
    }

    public List<AverageRating> findOttRecordInPagination(Integer pageno, Integer pagesize, String averagerate) {
        Pageable pageable = PageRequest.of(pageno,pagesize, Sort.by(averagerate));
        return averageRatingRepository.findAllByType("Ott",pageable).getContent();
    }
}
