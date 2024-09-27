package com.ikop.diet.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.text.MessageFormat;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FoodIdNotMatchForUpdateException extends RuntimeException {
    public FoodIdNotMatchForUpdateException(String foodIdToUpdate, String foodEntityId) {
        super(MessageFormat.format("Food ID in path {0} does not match food ID in entity to update {1}", foodIdToUpdate, foodEntityId));
    }
}
