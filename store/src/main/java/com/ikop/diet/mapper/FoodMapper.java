package com.ikop.diet.mapper;

import com.ikop.diet.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FoodMapper {
    public FoodDTO foodToFoodDto(Food food);

    public Food foodDtoToFood(FoodDTO foodDTO);

    public FoodToCreate foodCreateDtoToFoodCreate(FoodCreateDTO foodCreateDTO);

    @Mapping(target = "id", ignore = true)
    public Food foodCreateToFood(FoodToCreate foodToCreate);

    Food foodUpdateToFood(FoodUpdateDTO foodToUpdate);
}
