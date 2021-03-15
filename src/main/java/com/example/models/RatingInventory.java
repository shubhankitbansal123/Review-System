package com.example.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingInventory {

    private float quality=1;
    private float asAdvertised=1;
    private float satisfaction=1;

}
