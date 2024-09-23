package com.ikop.diet.controller;

import com.ikop.diet.mapper.FoodMapper;
import com.ikop.diet.model.*;
import com.ikop.diet.service.FoodService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/food")
@RequiredArgsConstructor
@Slf4j
public class FoodController {

    private final FoodService foodService;
    private final FoodMapper foodMapper;

    @GetMapping("/all")
    public @Valid ResponseEntity<Foods> getAllFoods() {
        log.info("request to get all foods in the Store");
        List<Food> allFoods = foodService.getAllFoods();
        log.info("total number of foods in catalog: {}", allFoods.size());
        return ResponseEntity.ok(new Foods(allFoods.stream().map(foodMapper::foodToFoodDto).toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoodDTO> getByFoodId(@PathVariable String id) {
        log.info("request to get a good with id {}", id);
        Food byId = foodService.findById(id);
        log.info("found food with id {}: {}", id, byId);
        return ResponseEntity.ok(foodMapper.foodToFoodDto(byId));
    }

    @PostMapping
    public @Valid ResponseEntity<FoodDTO> createFood(@Valid @RequestBody FoodCreateDTO foodToCreateDTO) {
        log.info("request to create a food {}", foodToCreateDTO);
        Food created = foodService.saveFood(foodMapper.foodCreateDtoToFoodCreate(foodToCreateDTO));
        log.info("food {} was created successfully", created);
        return new ResponseEntity(foodMapper.foodToFoodDto(created), HttpStatusCode.valueOf(201));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> updateFood(@PathVariable String id, @Valid @RequestBody FoodUpdateDTO foodToUpdate) {
        log.info("request to update food with id {} with new values: {}", id, foodToUpdate);
        foodService.updateFood(id, foodMapper.foodUpdateToFood(foodToUpdate));
        log.info("food id {} updated successfully", id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/search/{text}")
    public @Valid ResponseEntity<List<FoodDTO>> searchByText(@PathVariable String text) {
        log.info("request to search a food with the following string in name or description {}", text);
        List<FoodDTO> listOfMatchingFoods = foodService.searchForFood(text).stream().map(foodMapper::foodToFoodDto).toList();
        log.info("total number of matching foods found {}", listOfMatchingFoods.size());
        return ResponseEntity.ok(listOfMatchingFoods);
    }
}
