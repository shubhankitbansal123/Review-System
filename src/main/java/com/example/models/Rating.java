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
@Table(name = "Rating",uniqueConstraints = {@UniqueConstraint(columnNames = {"userid","hotelid"}),@UniqueConstraint(columnNames = {"userid","inventoryid"}),@UniqueConstraint(columnNames = {"userid","ottid"})})
@TypeDef(name = "jsonb",typeClass = JsonBinaryType.class)
public class Rating {

    @Id
    @Column(name = "ratingid")
    @JsonProperty
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ratingid;

    @Column(name = "userid")
    @JsonProperty
    private Integer userid;

    @Column(name = "hotelid")
    @JsonProperty
    private Integer hotelid=null;

    @Column(name = "ottid")
    @JsonProperty
    private Integer ottid=null;

    @Column(name = "inventoryid")
    @JsonProperty
    private Integer inventoryid=null;

    @JsonProperty
    private String type;

    @JsonProperty
    private String name;

    @JsonProperty
    @Type(type = "jsonb")
    @Column(name = "ratehotel",columnDefinition = "jsonb")
    private RatingHotel rateHotel = new RatingHotel();

    @JsonProperty
    @Type(type = "jsonb")
    @Column(name = "rateinventory",columnDefinition = "jsonb")
    private RatingInventory ratingInventory = new RatingInventory();

    @JsonProperty
    @Type(type = "jsonb")
    @Column(name = "rateott",columnDefinition = "jsonb")
    private RatingOtt ratingOtt = new RatingOtt();
}
