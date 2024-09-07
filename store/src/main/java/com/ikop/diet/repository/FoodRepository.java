package com.ikop.diet.repository;

import com.ikop.diet.model.Food;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface FoodRepository extends MongoRepository<Food, String> {

    @Query("{ '$text': { '$search': ?0 } }")
    List<Food> searchByText(String text);
}
