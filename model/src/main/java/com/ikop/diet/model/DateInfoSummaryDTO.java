package com.ikop.diet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DateInfoSummaryDTO {
    private double totalPoints;
    List<DiaryEntryDTO> entries;
}
