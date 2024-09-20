package com.ikop.diet.service;

import com.ikop.diet.mapper.FoodMapper;
import com.ikop.diet.model.Food;
import com.ikop.diet.model.FoodToCreate;
import com.ikop.diet.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodService {
    private final FoodRepository foodRepository;
    private final FoodMapper foodMapper;

    public Food saveFood(FoodToCreate foodToCreate) {
        return foodRepository.save(foodMapper.foodCreateToFood(foodToCreate));
    }

    public List<Food> getAllFoods() {
        return foodRepository.findAll();
    }

    public void updateFood(String foodIdToUpdate, Food foodToUpdate) {
        if (!foodIdToUpdate.equalsIgnoreCase(foodToUpdate.getId())) {
            throw new FoodIdNotMatchForUpdateException(foodIdToUpdate, foodToUpdate.getId());
        }
        if (!foodRepository.existsById(foodIdToUpdate)) {
            throw new FoodNotFoundException(foodIdToUpdate);
        }
        foodRepository.save(foodToUpdate);
    }

    public Food findById(String id) {
        return foodRepository.findById(id)
                .orElseThrow(() -> new FoodNotFoundException(id));
    }

    public List<Food> searchForFood(String textToSearch) {
        return foodRepository.searchByText(textToSearch);
    }


}
