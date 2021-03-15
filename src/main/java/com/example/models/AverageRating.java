package com.example.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@TypeDef(name = "jsonb",typeClass = JsonBinaryType.class)
@Table(name = "averagerating")
public class AverageRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    private Integer averageratingid;
    @JsonProperty
    private String name;
    @JsonProperty
    private Integer inventoryid=null;
    @JsonProperty
    private Integer ottid=null;
    @JsonProperty
    private String type;
    @Type(type = "jsonb")
    @Column(name = "rateaverageinventory",columnDefinition = "jsonb")
    @JsonProperty
    private RatingInventory rateAverageInventory = new RatingInventory(0,0,0);
    @Type(type = "jsonb")
    @Column(name = "rateaverageott",columnDefinition = "jsonb")
    @JsonProperty
    private RatingOtt rateAverageOtt = new RatingOtt(0,0,0,0,0);
    @JsonProperty
    private float averagerate=0;
    @JsonProperty
    private Integer numberofpeople=0;
}