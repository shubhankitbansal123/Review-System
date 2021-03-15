package com.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingKafka {
    private String usertoken;
    private Integer itemid=null;
    private Integer movieid=null;
    private RatingOtt ratingMovie=new RatingOtt(0,0,0,0,0);
    private RatingInventory ratingItem = new RatingInventory(0,0,0);
    private String itemName;
    private String movieName;
}
