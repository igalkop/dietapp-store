package com.ikop.diet.service;

import com.ikop.diet.model.DateInfoSummary;
import com.ikop.diet.model.DiaryEntry;
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
        return diaryEntryRepository.save(diaryEntry);
    }

    public List<DiaryEntry> getAllForDate(LocalDate date) {
        return diaryEntryRepository.findAllByDate(date);
    }

    public DateInfoSummary getAllEntriesForDate(LocalDate date) {
        List<DiaryEntry> allForDate = getAllForDate(date);
        Double totalForDate = getTotalForDate(allForDate);
        return new DateInfoSummary(totalForDate, allForDate);
    }

    private Double getTotalForDate(List<DiaryEntry> allForDate) {
        return allForDate
                .stream()
                .map(diaryEntry -> diaryEntry.getAmount() * diaryEntry.getFoodPoints())
                .reduce(0.0, Double::sum);
    }

    public void update(Long idToUpdate, DiaryEntry diaryEntryToUpdate) {
        if (!idToUpdate.equals(diaryEntryToUpdate.getId())) {
            throw new DiaryEntryNotMatchForUpdateException(idToUpdate.toString(), diaryEntryToUpdate.getId().toString());
        }
        if (!diaryEntryRepository.existsById(idToUpdate)) {
            throw new DiaryEntryNotFoundException(idToUpdate.toString());
        }
        diaryEntryRepository.save(diaryEntryToUpdate);
    }
}
