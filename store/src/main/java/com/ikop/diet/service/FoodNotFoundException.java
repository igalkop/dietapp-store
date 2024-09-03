package com.ikop.diet.service;

public class FoodNotFoundException extends RuntimeException {
    FoodNotFoundException(String msg) {
        super(msg);
    }

}
