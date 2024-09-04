package com.ikop.diet.controllers;

import com.ikop.diet.model.Day;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/status")
public class DayDiaryController {

    @GetMapping
    public ResponseEntity<Day> getCurrentDayStatus() {
        Day day = new Day();
        day.setId("111");
        day.setName("food1");
        day.setRemainingPoints(1.2);
        return ResponseEntity.ok(day);
    }

}
