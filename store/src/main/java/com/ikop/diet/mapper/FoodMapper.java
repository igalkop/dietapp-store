package com.ikop.diet.mapper;

import com.ikop.diet.model.Food;
import com.ikop.diet.model.FoodCreateDTO;
import com.ikop.diet.model.FoodDTO;
import com.ikop.diet.model.FoodToCreate;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FoodMapper {
    public FoodDTO foodToFoodDto(Food food);

    public Food foodDtoToFood(FoodDTO foodDTO);

    public FoodToCreate foodCreateDtoToFoodCreate(FoodCreateDTO foodCreateDTO);

    public Food foodCreateToFood(FoodToCreate foodToCreate);
}
