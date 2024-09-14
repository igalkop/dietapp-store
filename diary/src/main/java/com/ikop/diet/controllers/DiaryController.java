package com.ikop.diet.controllers;

import com.ikop.diet.model.DiaryEntry;
import com.ikop.diet.model.EntriesForDate;
import com.ikop.diet.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/diary")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

    @GetMapping("/{year}/{month}/{day}")
    public ResponseEntity<List<DiaryEntry>> getAllForDate(int year, int month, int day) {
        LocalDate date = LocalDate.of(year, month, day);
        List<DiaryEntry> allEntriesDate = diaryService.getAllForDate(date);
        return ResponseEntity.ok(allEntriesDate);
    }

    @PostMapping
    public ResponseEntity<DiaryEntry> createDiaryEntry(@RequestBody DiaryEntry diaryEntry) {
        DiaryEntry saved = diaryService.save(diaryEntry);
        return new ResponseEntity<>(saved, HttpStatusCode.valueOf(201));
    }

    @GetMapping("/points/{year}/{month}/{day}")
    public ResponseEntity<Double> getTotalPointsForDate(@PathVariable Integer year, @PathVariable Integer month, @PathVariable Integer day) {
        LocalDate date = LocalDate.of(year, month, day);
        double totalPointsForDate = diaryService.getTotalPointsForDate(date);
        return ResponseEntity.ok(totalPointsForDate);
    }

    @GetMapping("/list/{year}/{month}/{day}")
    public ResponseEntity<EntriesForDate> getAllEntriesForDay(@PathVariable Integer year, @PathVariable Integer month, @PathVariable Integer day) {
        LocalDate date = LocalDate.of(year, month, day);
        EntriesForDate EntriesForDate = diaryService.getAllEntriesForDate(date);
        return ResponseEntity.ok(EntriesForDate);
    }
}
