package com.ikop.diet.service;

import com.ikop.diet.model.DiaryEntry;
import com.ikop.diet.model.EntriesForDate;
import com.ikop.diet.repository.DiaryEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryEntryRepository diaryEntryRepository;

    public DiaryEntry save(DiaryEntry diaryEntry) {
        DiaryEntry saved = diaryEntryRepository.save(diaryEntry);
        return saved;
    }

    public List<DiaryEntry> getAllForDate(LocalDate date) {
        return diaryEntryRepository.findAllByDate(date);
    }

    public double getTotalPointsForDate(LocalDate date) {
        List<DiaryEntry> allForDate = getAllForDate(date);
        return getTotalForDate(allForDate);
    }

    public EntriesForDate getAllEntriesForDate(LocalDate date) {
        List<DiaryEntry> allForDate = getAllForDate(date);
        Double totalForDate = getTotalForDate(allForDate);
        return new EntriesForDate(totalForDate, allForDate);
    }

    private Double getTotalForDate(List<DiaryEntry> allForDate) {
        return allForDate
                .stream()
                .map(diaryEntry -> diaryEntry.getAmount() * diaryEntry.getFoodPoints())
                .reduce(0.0, Double::sum);
    }

}
