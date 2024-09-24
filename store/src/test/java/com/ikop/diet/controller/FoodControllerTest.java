package com.ikop.diet.controller;

import com.ikop.diet.model.FoodCreateDTO;
import com.ikop.diet.model.FoodDTO;
import com.ikop.diet.model.FoodUpdateDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCollection;

class FoodControllerTest {

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public static Stream<Arguments> dtoTestProvider() {
        return Stream.of(
                Arguments.of(new FoodDTO(), List.of(
                        "id cannot be empty",
                        "name cannot be empty",
                        "points cannot be null",
                        "description cannot be empty"
                )),
                Arguments.of(new FoodDTO("123", "name", 0d, "description"), List.of("minimal value of points is 0.1")),
                Arguments.of(new FoodUpdateDTO(), List.of(
                        "id cannot be empty",
                        "name cannot be empty",
                        "points cannot be null",
                        "description cannot be empty"
                )),
                Arguments.of(new FoodUpdateDTO("123", "name", 0d, "description"), List.of("minimal value of points is 0.1"))
        );
    }


    @ParameterizedTest
    @MethodSource("dtoTestProvider")
    void testDtosForValidation(FoodDTO inputFoodDto, List<String> expectedErrors) {
        Set<ConstraintViolation<FoodDTO>> errors = validator.validate(inputFoodDto);

        assertThat(errors).isNotEmpty();
        List<String> actualErrorList = errors.stream().map(ConstraintViolation::getMessage).toList();
        assertThatCollection(actualErrorList).containsExactlyInAnyOrderElementsOf(expectedErrors);
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