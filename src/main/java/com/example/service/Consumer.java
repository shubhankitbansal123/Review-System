package com.example.service;

import com.example.controller.RatingController;
import com.example.models.Rating;
import com.example.models.RatingKafka;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class Consumer {

    @Autowired
    private RatingController ratingController;

    @KafkaListener(topics = "Rating",groupId = "group_json",containerFactory = "containerFactory")
    public void consume(RatingKafka ratingKafka){
        System.out.println(ratingKafka);
        Rating rating = new Rating();
        rating.setRatingInventory(ratingKafka.getRatingItem());
        rating.setRatingOtt(ratingKafka.getRatingMovie());
        rating.setInventoryid(ratingKafka.getItemid());
        rating.setOttid(ratingKafka.getMovieid());
        if(ratingKafka.getItemid()!=null) {
            rating.setType("Inventory");
            rating.setName(ratingKafka.getItemName());
        }
        else if(ratingKafka.getMovieid()!=null) {
            rating.setType("Ott");
            rating.setName(ratingKafka.getMovieName());
        }
        System.out.println(ratingController.rateHotel(rating,ratingKafka.getUsertoken()));
    }
}
