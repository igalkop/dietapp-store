package com.ikop.diet.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.text.MessageFormat;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FoodNotFoundException extends RuntimeException {
    FoodNotFoundException(String id) {
        super(MessageFormat.format("Cannot find food with id {0}", id));
    }

}
