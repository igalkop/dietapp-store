package com.ikop.diet.model;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class FoodUpdateDTO extends FoodDTO {

    public FoodUpdateDTO(String id, String name, Double points, String description) {
        super(id, name, points, description);
    }

    public FoodUpdateDTO() {
        super();
    }
}
