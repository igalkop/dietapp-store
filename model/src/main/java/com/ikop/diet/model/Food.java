package com.ikop.diet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@Document(value = "foods")
public class Food {
    @Id
    private String id;
    @TextIndexed
    private String name;
    private double points;
    @TextIndexed
    private String description;
}
