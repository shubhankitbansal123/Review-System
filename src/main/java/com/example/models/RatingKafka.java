package com.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingKafka {
    private String usertoken;
    private Integer inventoryid=null;
    private Integer ottid=null;
    private RatingOtt ratingott=new RatingOtt();
    private RatingInventory ratingInventory = new RatingInventory();
    private String name;
}
