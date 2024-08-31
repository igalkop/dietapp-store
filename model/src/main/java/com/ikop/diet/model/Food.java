package com.ikop.diet.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Food {
    private final String name;
    private final int points;
}
