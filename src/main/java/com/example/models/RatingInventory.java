package com.example.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingInventory {
    private Double quality = 1.0;
    private Double asAdvertised = 1.0;
    private Double satisfaction = 1.0;
}
