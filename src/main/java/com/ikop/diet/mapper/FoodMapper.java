package com.ikop.diet.mapper;

import com.ikop.diet.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FoodMapper {
    FoodDTO foodToFoodDto(Food food);

    FoodToCreate foodCreateDtoToFoodCreate(FoodCreateDTO foodCreateDTO);

    @Mapping(target = "id", ignore = true)
    Food foodCreateToFood(FoodToCreate foodToCreate);

    Food foodUpdateToFood(FoodUpdateDTO foodToUpdate);
}
