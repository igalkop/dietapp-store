package com.ikop.diet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@Document("foods")
public class Food {
    @Id
    private String id;
    private final String name;
    private final double points;
}
