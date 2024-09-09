package com.ikop.diet.controller;

import com.ikop.diet.model.Food;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.net.MalformedURLException;
import java.net.URI;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


/**
 * https://github.com/testcontainers/testcontainers-java-spring-boot-quickstart/blob/main/README.md
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class FoodControllerIT {

    @LocalServerPort
    private Integer port;

    private String host = "localhost";

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Container
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.1");

    @BeforeEach
    public void setup() {

    }

    @Test
    void getAllFoods() throws MalformedURLException {
//        System.out.println("Spring context created");
//        Food saved = foodRepository.save(new Food(null, "foo1", 1.5, "some"));
//        List<Food> all = foodRepository.findAll();
//        all.forEach(food -> System.out.println(food.toString()));

        URI urlSave = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(host)
                .port(port)
                .path("/store/food")
                .build().toUri();

        ResponseEntity<Food> created = testRestTemplate.postForEntity(urlSave, new Food(null, "food name", 2.5, "some desc"), Food.class);
        System.out.println("create status code = " + created.getStatusCode().value());

        URI url = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(host)
                .port(port)
                .path("/store/food/all")
                .build().toUri();
//
        ResponseEntity<Foods> response = testRestTemplate.getForEntity(url, Foods.class);
        System.out.println("get all status code = " + response.getStatusCode().value());
        System.out.println("response.getBody().getFoods().size() = " + response.getBody().getFoods().size());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
    }
}