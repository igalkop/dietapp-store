package com.ikop.diet.service;

import com.ikop.diet.mapper.FoodMapper;
import com.ikop.diet.model.Food;
import com.ikop.diet.model.FoodToCreate;
import com.ikop.diet.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
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
            log.error("request to update food id {} while the Food entity id is {}. Aborting.", foodIdToUpdate, foodToUpdate.getId());
            throw new FoodIdNotMatchForUpdateException(foodIdToUpdate, foodToUpdate.getId());
        }
        if (!foodRepository.existsById(foodIdToUpdate)) {
            log.error("request to update food id {} while such food does not exists. Aborting", foodIdToUpdate);
            throw new FoodNotFoundException(foodIdToUpdate);
        }
        foodRepository.save(foodToUpdate);
    }

    public Food findById(String id) {
        return foodRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Food with id {} was not found in DB.", id);
                    return new FoodNotFoundException(id);
                });
    }

    public List<Food> searchForFood(String textToSearch) {
        return foodRepository.searchByText(textToSearch);
    }


}
