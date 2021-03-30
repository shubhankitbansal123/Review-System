package com.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingKafka implements Serializable {
    private Integer clientId;
    private String clientName;
    private String clientPassword;
    private Integer typeId=null;
    private Map<String,Double> rating=new HashMap<>();
    private String name;
    private String type;
}
