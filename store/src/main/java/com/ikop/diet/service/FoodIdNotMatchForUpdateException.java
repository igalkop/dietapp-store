package com.ikop.diet.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FoodIdNotMatchForUpdateException extends RuntimeException {
    public FoodIdNotMatchForUpdateException(String foodIdToUpdate, String foodEntityId) {
        super(String.format("Food ID in path {} does not match food ID in entity to update {}", foodIdToUpdate, foodEntityId));
    }
}
