package com.ikop.diet.mapper;

import com.ikop.diet.model.*;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FoodMapper {
    public FoodDTO foodToFoodDto(Food food);

    public Food foodDtoToFood(FoodDTO foodDTO);

    public FoodToCreate foodCreateDtoToFoodCreate(FoodCreateDTO foodCreateDTO);

    public Food foodCreateToFood(FoodToCreate foodToCreate);

    Food foodUpdateToFood(FoodUpdateDTO foodToUpdate);
}
