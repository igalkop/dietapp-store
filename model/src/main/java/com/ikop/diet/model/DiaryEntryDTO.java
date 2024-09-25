package com.ikop.diet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DiaryEntryDTO {
    private Long id;
    private String foodName;
    private double foodPoints;
    private double amount;
    private LocalDate date;
}
