package com.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingKafka {
    private String token;
    private Integer typeid=null;
    private Map<String,Double> rating=new HashMap<>();
    private String name;
    private String type;
}
