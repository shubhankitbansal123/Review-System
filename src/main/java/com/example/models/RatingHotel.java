package com.example.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingHotel {

    private Double food = 1.0;
    private Double locality = 1.0;
    private Double hygine = 1.0;
    private Double security = 1.0;
    private Double service = 1.0;
}
