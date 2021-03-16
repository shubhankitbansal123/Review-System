package com.example.service;

import com.example.controller.RatingController;
import com.example.models.Rating;
import com.example.models.RatingInventory;
import com.example.models.RatingKafka;
import com.example.models.RatingOtt;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class Consumer {

    @Autowired
    private RatingController ratingController;

    @KafkaListener(topics = "Rating",groupId = "group_json",containerFactory = "containerFactory")
    public void consume(RatingKafka ratingKafka){
        System.out.println(ratingKafka);
        Rating rating = new Rating();
        rating.setTypeid(ratingKafka.getTypeid());
        rating.setName(ratingKafka.getName());
        rating.setType(ratingKafka.getType());
        ObjectMapper objectMapper = new ObjectMapper();
        if(ratingKafka.getType().equalsIgnoreCase("Inventory")){
            RatingInventory ratingInventory = objectMapper.convertValue(ratingKafka.getRating(), RatingInventory.class);
            rating.setRating(objectMapper.convertValue(ratingInventory,Map.class));
        }
        else if(ratingKafka.getType().equalsIgnoreCase("Ott")){
            RatingOtt ratingOtt = objectMapper.convertValue(ratingKafka.getRating(), RatingOtt.class);
            rating.setRating(objectMapper.convertValue(ratingOtt,Map.class));
        }
        System.out.println(ratingController.rateHotel(rating,ratingKafka.getToken()));
    }
}
