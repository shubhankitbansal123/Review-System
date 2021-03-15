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
public class Comment {

    @Id
    @JsonProperty
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commentid;

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
    private String comment;

    @JsonProperty
    private String type;

    @JsonProperty
    private String name;
}
