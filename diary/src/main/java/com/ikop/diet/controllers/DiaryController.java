package com.ikop.diet.controllers;

import com.ikop.diet.model.DateInfoSummary;
import com.ikop.diet.model.DiaryEntry;
import com.ikop.diet.service.DiaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class DiaryController {

    private final DiaryService diaryService;


    @PostMapping
    public ResponseEntity<DiaryEntry> createDiaryEntry(@RequestBody DiaryEntry diaryEntry) {
        log.info("request to create a Diary Entry: {}", diaryEntry);
        DiaryEntry saved = diaryService.save(diaryEntry);
        log.info("Diary Entry successfully created");
        return new ResponseEntity<>(saved, HttpStatusCode.valueOf(201));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateDiaryEntry(@PathVariable Long id, @RequestBody DiaryEntry diaryEntry) {
        log.info("request to update a Diary Entry: {}", diaryEntry);
        diaryService.update(id, diaryEntry);
        log.info("Diary Entry successfully updated");
        return ResponseEntity.ok(null);
    }

    @GetMapping("/list/{year}/{month}/{day}")
    public ResponseEntity<DateInfoSummary> getDateSummary(@PathVariable Integer year, @PathVariable Integer month, @PathVariable Integer day) {
        LocalDate date = LocalDate.of(year, month, day);
        log.info("request to get diary summery for date: {}", date);
        DateInfoSummary allEntriesForDate = diaryService.getAllEntriesForDate(date);
        return ResponseEntity.ok(allEntriesForDate);
    }
}
