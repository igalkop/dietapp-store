package com.ikop.diet.service;

import com.ikop.diet.model.Food;
import com.ikop.diet.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodService {
    private final FoodRepository foodRepository;

    public Food saveFood(Food food) {
        return foodRepository.save(food);
    }

    public List<Food> getAllFoods() {
        return foodRepository.findAll();
    }

    public Food findById(String id) {
        return foodRepository.findById(id)
                .orElseThrow(() -> new FoodNotFoundException("cannot find food with id " + id));
    }

    public List<Food> searchForFood(String textToSearch) {
        return foodRepository.searchByText(textToSearch);
    }


}
