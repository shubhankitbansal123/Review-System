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
    private Producer producer;

    @PostMapping("/rateKafka")
    public String publish(@RequestBody RatingKafka ratingKafka){
        producer.publish(ratingKafka);
        return "Updated";
    }
}
