package com.example.controller;

import com.example.Exception.GenericException;
import com.example.models.RatingKafka;
import com.example.service.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger logger = LoggerFactory.getLogger(RateKafkaController.class);

    public RateKafkaController(Producer producer) {
        this.producer = producer;
    }

    @PostMapping("/rateKafka")
    public ResponseEntity<Object> publish(@RequestBody RatingKafka ratingKafka) throws GenericException {
        logger.info("Rate Kafka api is running");
        try {
            producer.publish(ratingKafka);
            logger.info("Data Published is Successful");
            return new ResponseEntity<>("Data Published Successfully", HttpStatus.OK);
        }catch (Exception e){
            logger.error("Data published Unsuccessful");
            throw new GenericException("Data is not published");
        }
    }
}
