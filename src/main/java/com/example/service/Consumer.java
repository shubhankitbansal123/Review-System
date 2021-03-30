package com.example.service;

import com.example.Exception.DuplicateEntryException;
import com.example.Exception.GenericException;
import com.example.controller.RatingController;
import com.example.models.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Map;
import java.util.UUID;

@Service
public class Consumer {

    @Autowired
    private final RatingController ratingController;

    @Autowired
    private final UserService userService;

    public Consumer(RatingController ratingController, UserService userService) {
        this.ratingController = ratingController;
        this.userService = userService;
    }

    @KafkaListener(topics = "Rating",groupId = "group_json",containerFactory = "containerFactory")
    public void consume(String ratingKafka1) throws GenericException {
        System.out.println(ratingKafka1);
//        ObjectMapper objectMapper = new ObjectMapper();
        Gson gson = new Gson();
        RatingKafka ratingKafka = gson.fromJson(ratingKafka1,RatingKafka.class);
        System.out.println("json"+ ratingKafka);
        if (ObjectUtils.isEmpty(ratingKafka.getClientName()) || ObjectUtils.isEmpty(ratingKafka.getType()) ||
                ObjectUtils.isEmpty(ratingKafka.getClientPassword()) || ObjectUtils.isEmpty(ratingKafka.getRating()) ||
                ObjectUtils.isEmpty(ratingKafka.getName()) || ObjectUtils.isEmpty(ratingKafka.getClientId()) ||
                ObjectUtils.isEmpty(ratingKafka.getTypeId())){
            System.out.println("Some fields are missing");
            return;
        }
        if(!ratingKafka.getType().equalsIgnoreCase("Inventory") && !ratingKafka.getType().equalsIgnoreCase("Ott")){
            return;
        }
        boolean count = userService.findByUsernameAndType(ratingKafka.getClientName(),ratingKafka.getType());
        if(!count) {
            Users users = new Users();
            users.setUserid(ratingKafka.getClientId());
            users.setUsername(ratingKafka.getClientName());
            users.setPassword(ratingKafka.getClientPassword());
            users.setType(ratingKafka.getType());
            users.setIsadmin(false);
            String usertoken = UUID.randomUUID().toString();
            users.setUsertoken(usertoken);
            userService.save(users, usertoken);
        }

        Rating rating = new Rating();
        rating.setTypeid(ratingKafka.getTypeId());
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
        String usertoken = userService.getUserTokenByUserIdAndType(ratingKafka.getClientId(),ratingKafka.getType());
        try {
            System.out.println(ratingController.rateHotel(rating, usertoken));
        }catch (DataIntegrityViolationException e){
            throw new DuplicateEntryException("Duplicate data not allowed");
        }
    }
}
