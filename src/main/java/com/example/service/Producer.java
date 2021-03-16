package com.example.service;

import com.example.models.RatingKafka;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class Producer {

    @Autowired
    private KafkaTemplate<String, RatingKafka> kafkaTemplate;

    private String TOPIC = "Rating";

    public void publish(RatingKafka ratingKafka){
        kafkaTemplate.send(TOPIC,ratingKafka);
    }
}
