package com.ikop.diet.controller;

import com.ikop.diet.model.Food;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;


@RequiredArgsConstructor
@Getter
public class Foods {
    private final List<Food> foods;
}
