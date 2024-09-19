package com.ikop.diet.controllers;

import com.ikop.diet.model.DiaryEntry;
import com.ikop.diet.model.EntriesForDate;
import com.ikop.diet.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;


    @PostMapping
    public ResponseEntity<DiaryEntry> createDiaryEntry(@RequestBody DiaryEntry diaryEntry) {
        DiaryEntry saved = diaryService.save(diaryEntry);
        return new ResponseEntity<>(saved, HttpStatusCode.valueOf(201));
    }

    @GetMapping("/list/{year}/{month}/{day}")
    public ResponseEntity<EntriesForDate> getAllEntriesForDay(@PathVariable Integer year, @PathVariable Integer month, @PathVariable Integer day) {
        LocalDate date = LocalDate.of(year, month, day);
        EntriesForDate allEntriesForDate = diaryService.getAllEntriesForDate(date);
        return ResponseEntity.ok(allEntriesForDate);
    }
}
