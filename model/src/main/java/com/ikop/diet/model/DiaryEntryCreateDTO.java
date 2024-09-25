package com.ikop.diet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DiaryEntryCreateDTO {
    private String foodName;
    private double foodPoints;
    private double amount;
    private LocalDate date;
}
