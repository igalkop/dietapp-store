package com.ikop.diet.controller;

import com.ikop.diet.model.Food;
import com.ikop.diet.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/food")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;

    @GetMapping("/all")
    public ResponseEntity<Foods> getAllFoods() {
        List<Food> allFoods = foodService.getAllFoods();
        return ResponseEntity.ok(new Foods(allFoods));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Food> getByFoodId(@PathVariable String id) {
        Food byId = foodService.findById(id);
        return ResponseEntity.ok(byId);
    }

    @PostMapping
    public ResponseEntity<Food> createFood(@RequestBody Food foodToCreate) {
        Food created = foodService.saveFood(foodToCreate);
        return ResponseEntity.ok(created);
    }
}
