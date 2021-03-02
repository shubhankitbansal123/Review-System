package com.example.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Comment {


    @JsonProperty
    private Integer user_id;
    @JsonProperty
    private Integer type_id;
    @JsonProperty
    private String comment;
    @JsonProperty
    private String type;
    @Id
    @JsonProperty
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer comment_id;
}
