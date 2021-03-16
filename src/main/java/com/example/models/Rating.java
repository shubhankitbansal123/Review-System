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
@Table(name = "Rating",uniqueConstraints = @UniqueConstraint(columnNames = {"userid","typeid","type"}))
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

    @Column(name = "typeid")
    @JsonProperty
    private Integer typeid=null;

    @JsonProperty
    private String type;

    @JsonProperty
    private String name;

    @JsonProperty
    @Type(type = "jsonb")
    @Column(name = "rating",columnDefinition = "jsonb")
    private Map<String,Double> rating = new HashMap<>();
}
