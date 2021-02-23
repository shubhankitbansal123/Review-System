package com.example.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(RatingId.class)
@Table(name = "Rating")
public class Rating {

    @Id
    @Column(name = "user_id")
    @JsonProperty
    private Integer user_id;

    @Id
    @Column(name = "hotel_id")
    @JsonProperty
    private Integer hotel_id;

    @JsonProperty
    @Column(name = "rate")
    private String rate;
}
