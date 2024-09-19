package com.ikop.diet.controller;

import com.ikop.diet.mapper.FoodMapper;
import com.ikop.diet.model.Food;
import com.ikop.diet.model.FoodCreateDTO;
import com.ikop.diet.model.FoodDTO;
import com.ikop.diet.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/food")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;
    private final FoodMapper foodMapper;

    @GetMapping("/all")
    public ResponseEntity<Foods> getAllFoods() {
        List<Food> allFoods = foodService.getAllFoods();
        return ResponseEntity.ok(new Foods(allFoods.stream().map(foodMapper::foodToFoodDto).toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoodDTO> getByFoodId(@PathVariable String id) {
        Food byId = foodService.findById(id);
        return ResponseEntity.ok(foodMapper.foodToFoodDto(byId));
    }

    @PostMapping
    public ResponseEntity<FoodDTO> createFood(@RequestBody FoodCreateDTO foodToCreateDTO) {
        Food created = foodService.saveFood(foodMapper.foodCreateDtoToFoodCreate(foodToCreateDTO));
        return new ResponseEntity(foodMapper.foodToFoodDto(created), HttpStatusCode.valueOf(201));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> updateFood(@PathVariable String id, @RequestBody Food foodToUpdate) {
        foodService.updateFood(id, foodToUpdate);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/search/{text}")
    public ResponseEntity<List<FoodDTO>> searchByText(@PathVariable String text) {
        return ResponseEntity.ok(foodService.searchForFood(text).stream().map(foodMapper::foodToFoodDto).toList());
    }
}
