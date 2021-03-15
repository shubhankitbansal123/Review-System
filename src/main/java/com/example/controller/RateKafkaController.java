package com.example.controller;

import com.example.models.RatingKafka;
import com.example.service.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RateKafkaController {

    @Autowired
    private final Producer producer;

    public RateKafkaController(Producer producer) {
        this.producer = producer;
    }

    @PostMapping("/rateKafka")
    public String publish(@RequestBody RatingKafka ratingKafka){
        try {
            producer.publish(ratingKafka);
            return "Updated";
        }catch (Exception e){
            return "Data is not published";
        }
    }
}
