package com.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Comparator;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllRecords {

    @Id
    private Integer type_id;
    private String type;
    private float averageRate;
    private List<String> comments;
}
