package com.example.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Rating", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","type_id"}))
@TypeDef(name = "jsonb",typeClass = JsonBinaryType.class)
public class Rating {

    @Id
    @Column(name = "rating_id")
    @JsonProperty
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer rating_id;

    @Column(name = "user_id")
    @JsonProperty
    private Integer user_id;

    @Column(name = "type_id")
    @JsonProperty
    private Integer type_id;

    @JsonProperty
    private String type;

    @JsonProperty
    @Type(type = "jsonb")
    @Column(name = "rate_hotel",columnDefinition = "jsonb")
    private RatingHotel rateHotel = new RatingHotel();

    @JsonProperty
    @Type(type = "jsonb")
    @Column(name = "rate_inventory",columnDefinition = "jsonb")
    private RatingInventory ratingInventory = new RatingInventory();

    @JsonProperty
    @Type(type = "jsonb")
    @Column(name = "rate_ott",columnDefinition = "jsonb")
    private RatingOtt ratingOtt = new RatingOtt();
}
