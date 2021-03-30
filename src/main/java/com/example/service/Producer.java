package com.example.service;

import com.example.models.RatingKafka;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class Producer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private String TOPIC = "Rating";

    public void publish(RatingKafka ratingKafka) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.writeValueAsString(ratingKafka);
        kafkaTemplate.send(TOPIC,objectMapper.writeValueAsString(ratingKafka));
    }

}
