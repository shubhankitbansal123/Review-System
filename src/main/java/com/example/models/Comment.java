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
@Table(name = "Comment")
public class Comment {

    @Id
    @JsonProperty
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commentid;

    @JsonProperty
    private Integer userid;

    @Column(name = "typeid")
    @JsonProperty
    private Integer typeid=null;

    @JsonProperty
    private String comment;

    @JsonProperty
    private String type;

    @JsonProperty
    private String name;
}
