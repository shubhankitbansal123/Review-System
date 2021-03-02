package com.example.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingParameter {

    private float food = 0f;
    private float service = 0f;
    private float locality = 0f;
    private float hygine = 0f;
    private float security = 0f;
}
