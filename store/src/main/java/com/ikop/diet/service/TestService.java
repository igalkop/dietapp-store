package com.ikop.diet.service;

import com.ikop.diet.model.Food;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    public TestService() {
        Food food = new Food("milk", 3);
        System.out.println("food = " + food);
    }
}
