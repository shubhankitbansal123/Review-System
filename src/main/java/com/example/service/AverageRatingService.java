package com.example.service;

import com.example.models.AverageRating;
import com.example.repository.AverageRatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AverageRatingService {

    @Autowired
    private AverageRatingRepository averageRatingRepository;

    public void updateRatingInventory(Map<String,Double> ratingInventory, Double average, Integer inventoryid) {
        averageRatingRepository.updateRatingInventory(inventoryid,average,ratingInventory.get("quality"),ratingInventory.get("asAdvertised"),ratingInventory.get("satisfaction"));
    }

    public void updateRatingOtt(Map<String,Double> ratingOtt, Double average, Integer ottid) {
        averageRatingRepository.updateRatingOtt(ottid,average,ratingOtt.get("directionAndStory"),ratingOtt.get("actorsPerformance"),ratingOtt.get("productionValues"));
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
        Pageable pageable = PageRequest.of(pageno,pagesize, Sort.by(averagerate).descending());
        return averageRatingRepository.findAllByType("Inventory",pageable).getContent();
    }

    public List<AverageRating> findOttRecordInPagination(Integer pageno, Integer pagesize, String averagerate) {
        Pageable pageable = PageRequest.of(pageno,pagesize, Sort.by(averagerate).descending());
        return averageRatingRepository.findAllByType("Ott",pageable).getContent();
    }

    public AverageRating getAverageRatingAndPeople(Integer typeId, String type) {
        return averageRatingRepository.getAverageRatingAndPeople(typeId,type);
    }
}
