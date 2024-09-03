package com.ikop.diet.service;

import com.ikop.diet.model.Food;
import com.ikop.diet.repository.FoodRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestService {

    private final FoodRepository foodRepository;

//    public TestService() {
//        Food food = new Food("milk", 3);
//        System.out.println("food = " + food);
//    }

//    @PostConstruct
//    public void init(){
//        foodRepository.save(new Food("milk", 10));
//        System.out.println("listing all foods so far...");
//        foodRepository.findAll().forEach(System.out::println);
//    }
}
