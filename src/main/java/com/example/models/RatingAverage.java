package com.example.models;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(name = "jsonb",typeClass = JsonBinaryType.class)
public class RatingAverage {
    @Column(name = "type_id")
    private Integer type_id;

    @Type(type = "jsonb")
    @Column(name = "average", columnDefinition = "jsonb")
    private RatingParameter ratingParameter1 = new RatingParameter();

}
