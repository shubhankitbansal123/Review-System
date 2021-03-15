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
        rating.setInventoryid(ratingKafka.getInventoryid());
        rating.setRatingOtt(ratingKafka.getRatingott());
        if(ratingKafka.getInventoryid()!=null) {
            rating.setType("Inventory");
        }
        else if(ratingKafka.getOttid()!=null) {
            rating.setType("Ott");
        }
        rating.setRatingInventory(ratingKafka.getRatingInventory());
        rating.setRatingOtt(ratingKafka.getRatingott());
        rating.setName(ratingKafka.getName());
        System.out.println(ratingController.rateHotel(rating,ratingKafka.getUsertoken()));
    }
}
