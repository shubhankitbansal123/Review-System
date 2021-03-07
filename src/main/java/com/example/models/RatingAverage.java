package com.example.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(name = "jsonb",typeClass = JsonBinaryType.class)
public class RatingAverage {

    @Column(name = "type")
    private String type;

    @Column(name = "type_id")
    private Integer type_id;

    @JsonProperty
    @Type(type = "jsonb")
    @Column(name = "averagehotel")
    private RatingHotel ratingHotel = new RatingHotel();

    @JsonProperty
    @Type(type = "jsonb")
    @Column(name = "averageinventory")
    private RatingInventory ratingInventory = new RatingInventory();

    @JsonProperty
    @Type(type = "jsonb")
    @Column(name = "averageott")
    private RatingOtt ratingOtt = new RatingOtt();
}
