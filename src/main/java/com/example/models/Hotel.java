package com.example.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
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
    private String contact;
    @JsonProperty
    @Type(type = "jsonb")
    @Column(name = "rateaveragehotel",columnDefinition = "jsonb")
    private RatingHotel rateAverageHotel = new RatingHotel(0,0,0,0,0);
    @JsonProperty
    private float averagerating=0;
    @JsonProperty
    private Integer noofpeople=0;

    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "hotelid")
    private Set<Rating> ratings;

    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "hotelid")
    private Set<Comment> comments;

}
