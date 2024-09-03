package com.ikop.diet.repository;

import com.ikop.diet.model.Food;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FoodRepository extends MongoRepository<Food, Integer> {
}
