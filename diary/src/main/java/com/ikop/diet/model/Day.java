package com.ikop.diet.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class Day {
    @Id
    private String id;
    private String name;
    private double remainingPoints = 0.0;

}
