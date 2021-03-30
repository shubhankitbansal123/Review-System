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
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Hotel")
@TypeDef(name = "jsonb",typeClass = JsonBinaryType.class)
public class Hotel {

    @Id
    @Column(name = "hotelid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    private Integer hotelid;
    @JsonProperty
    private String hotelname;
    @JsonProperty
    private String location;
    @JsonProperty
    @Column(name = "contact",unique = true)
    private String contact;
    @JsonProperty
    @Type(type = "jsonb")
    @Column(name = "rating",columnDefinition = "jsonb")
    private Map<String,Double> rating = new HashMap<>();
    @JsonProperty
    private Double averagerating=0.0;
    @JsonProperty
    private Integer noofpeople=0;

}
