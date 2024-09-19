package com.ikop.diet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EntriesForDate {
    private double totalPoints;
    List<DiaryEntry> entries;
}