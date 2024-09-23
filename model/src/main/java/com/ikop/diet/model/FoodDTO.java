package com.ikop.diet.model;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FoodDTO {
    @NotNull
    private String id;

    @NotBlank(message = "name cannot be empty")
    private String name;

    @NotNull
    @DecimalMin(value = "0.01", message = "minimal value of points is 0.1")
    private double points;

    @NotBlank(message = "description cannot be empty")
    private String description;
}
