package com.ikop.diet.controllers;

import com.ikop.diet.model.DiaryEntry;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DiaryControllerIT {


    @LocalServerPort
    private int port;

    @Autowired
    @PersistenceContext
    private EntityManager entityManager;

    @Container
    @ServiceConnection
    static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8");

    @Test
    @Transactional
    void name() {
        DiaryEntry diaryEntry = new DiaryEntry(null, "food1", 3.3, 2, LocalDate.now());

        entityManager.persist(diaryEntry);
        System.out.println("saved");
        DiaryEntry found = entityManager.find(DiaryEntry.class, diaryEntry.getId());
        assertThat(found).isEqualTo(diaryEntry);
    }
}