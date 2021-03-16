package com.example.controller;

import com.example.models.RatingKafka;
import com.example.service.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Object> publish(@RequestBody RatingKafka ratingKafka){
        try {
            producer.publish(ratingKafka);
            return new ResponseEntity<>("Data Published Successfully", HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity<>("Data is not published",HttpStatus.BAD_REQUEST);
        }
    }
}
