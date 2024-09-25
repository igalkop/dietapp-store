package com.ikop.diet.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DiaryEntryCreateDTO {
    @NotBlank(message = "foodName cannot be empty")
    private String foodName;

    @NotNull(message = "foodPoints cannot be empty")
    @Positive(message = "foodPoints must be positive")
    private Double foodPoints;

    @NotNull(message = "amount cannot be empty")
    @Positive(message = "amount must be positive")
    private Double amount;

    @NotNull(message = "date cannot be empty")
    @PastOrPresent(message = "date must be today or previous date")
    private LocalDate date;
}
