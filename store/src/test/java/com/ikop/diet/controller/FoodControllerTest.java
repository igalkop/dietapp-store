package com.ikop.diet.controller;

import com.ikop.diet.model.FoodCreateDTO;
import com.ikop.diet.model.FoodDTO;
import com.ikop.diet.model.FoodUpdateDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCollection;

class FoodControllerTest {

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();


    @Test
    void validateEmptyFoodDtoBean() {
        List<String> expectedMessages = List.of(
                "id cannot be empty",
                "name cannot be empty",
                "points cannot be null",
                "description cannot be empty"
        );
        FoodDTO foodDTO = new FoodDTO();

        Set<ConstraintViolation<FoodDTO>> errors = validator.validate(foodDTO);

        assertThat(errors).isNotEmpty();
        List<String> actualErrorList = errors.stream().map(ConstraintViolation::getMessage).toList();
        assertThatCollection(actualErrorList).containsExactlyInAnyOrderElementsOf(expectedMessages);
    }

    @Test
    void assertForNegativePointsFoodDto() {
        List<String> expectedMessages = List.of("minimal value of points is 0.1");
        FoodDTO foodDTO = new FoodDTO("123", "name", 0d, "description");


        Set<ConstraintViolation<FoodDTO>> errors = validator.validate(foodDTO);

        assertThat(errors).isNotEmpty();
        List<String> actualErrorList = errors.stream().map(ConstraintViolation::getMessage).toList();
        assertThatCollection(actualErrorList).containsExactlyInAnyOrderElementsOf(expectedMessages);
    }

    @Test
    void validateEmptyFoodUpdateDtoBean() {
        List<String> expectedMessages = List.of(
                "id cannot be empty",
                "name cannot be empty",
                "points cannot be null",
                "description cannot be empty"
        );
        FoodUpdateDTO foodUpdateDTO = new FoodUpdateDTO();

        Set<ConstraintViolation<FoodUpdateDTO>> errors = validator.validate(foodUpdateDTO);

        assertThat(errors).isNotEmpty();
        List<String> actualErrorList = errors.stream().map(ConstraintViolation::getMessage).toList();
        assertThatCollection(actualErrorList).containsExactlyInAnyOrderElementsOf(expectedMessages);
    }

    @Test
    void assertForNegativePointsFoodUpdateDto() {
        List<String> expectedMessages = List.of("minimal value of points is 0.1");
        FoodUpdateDTO foodUpdateDTO = new FoodUpdateDTO("123", "name", 0d, "description");


        Set<ConstraintViolation<FoodUpdateDTO>> errors = validator.validate(foodUpdateDTO);

        assertThat(errors).isNotEmpty();
        List<String> actualErrorList = errors.stream().map(ConstraintViolation::getMessage).toList();
        assertThatCollection(actualErrorList).containsExactlyInAnyOrderElementsOf(expectedMessages);
    }

    @Test
    void validateEmptyFoodCreateDtoBean() {
        List<String> expectedMessages = List.of(
                "name cannot be empty",
                "points cannot be null",
                "description cannot be empty"
        );
        FoodCreateDTO foodCreateDTO = new FoodCreateDTO();

        Set<ConstraintViolation<FoodCreateDTO>> errors = validator.validate(foodCreateDTO);

        assertThat(errors).isNotEmpty();
        List<String> actualErrorList = errors.stream().map(ConstraintViolation::getMessage).toList();
        assertThatCollection(actualErrorList).containsExactlyInAnyOrderElementsOf(expectedMessages);
    }

    @Test
    void assertForNegativePointsFoodCreateDto() {
        List<String> expectedMessages = List.of("minimal value of points is 0.1");
        FoodCreateDTO foodCreateDTO = new FoodCreateDTO("name", 0d, "description");


        Set<ConstraintViolation<FoodCreateDTO>> errors = validator.validate(foodCreateDTO);

        assertThat(errors).isNotEmpty();
        List<String> actualErrorList = errors.stream().map(ConstraintViolation::getMessage).toList();
        assertThatCollection(actualErrorList).containsExactlyInAnyOrderElementsOf(expectedMessages);
    }
}