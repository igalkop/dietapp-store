package com.ikop.diet.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FoodDTO {
    private String id;
    private String name;
    private double points;
    private String description;
}
