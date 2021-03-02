package com.example.models;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(name = "jsonb",typeClass = JsonBinaryType.class)
public class RatingAverage {
    @Column(name = "type_id")
    private Integer type_id;

    @Type(type = "jsonb")
    @Column(name = "average", columnDefinition = "jsonb")
    private Map<String,Float> average = new HashMap<>();

    public void addRate(String key,Float value){
        average.put(key,value);
    }
}
