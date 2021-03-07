package com.example.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingHotel {

    private float food=1;
    private float service=1 ;
    private float locality=1;
    private float hygine=1;
    private float security=1;
}
