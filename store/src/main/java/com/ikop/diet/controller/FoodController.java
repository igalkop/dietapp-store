package com.ikop.diet.controller;

import com.ikop.diet.model.Food;
import com.ikop.diet.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
        try {
            Food byId = foodService.findById(id);
            return ResponseEntity.ok(byId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Food> createFood(@RequestBody Food foodToCreate) {
        Food created = foodService.saveFood(foodToCreate);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/search/{text}")
    public ResponseEntity<List<Food>> searchByText(@PathVariable String text) {
        return ResponseEntity.ok(foodService.searchForFood(text));
    }
}
