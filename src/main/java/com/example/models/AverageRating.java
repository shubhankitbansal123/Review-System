package com.example.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

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
    private Integer typeid=null;
    @JsonProperty
    private String type;
    @Type(type = "jsonb")
    @Column(name = "rating",columnDefinition = "jsonb")
    @JsonProperty
    private Map<String,Double> rating = new HashMap<>();
    @JsonProperty
    private Double averagerate=0.0;
    @JsonProperty
    private Integer numberofpeople=0;
}