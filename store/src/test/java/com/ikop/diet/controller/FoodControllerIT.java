package com.ikop.diet.controller;

import com.ikop.diet.model.Food;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


/**
 * https://github.com/testcontainers/testcontainers-java-spring-boot-quickstart/blob/main/README.md
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class FoodControllerIT {

    public static final String COLLECTION_NAME = "foods";
    @LocalServerPort
    private Integer port;

    private final String HOST = "localhost";

    @Autowired
    private TestRestTemplate testRestTemplate;

    private URI urlGetAllFoods;


    @Container
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.1");

    @Autowired
    MongoTemplate mongoTemplate;

    @BeforeEach
    public void setup() {
        removeAllFromCollection();

        urlGetAllFoods = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(HOST)
                .port(port)
                .path("/store/food/all")
                .build().toUri();


    }

    @Test
    void getFoodById() {
        String idPath = "/store/food/FOOD_ID";


        Food foodToCreate = new Food(null, "food1", 11, "description 1");
        mongoTemplate.insert(foodToCreate);
        idPath = idPath.replace("FOOD_ID", foodToCreate.getId());
        URI urlGetFoodById = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(HOST)
                .port(port)
                .path(idPath)
                .build().toUri();

        ResponseEntity<Food> getFoodByIdResponse = testRestTemplate.getForEntity(urlGetFoodById, Food.class);

        assertThat(getFoodByIdResponse.getStatusCode().value()).isEqualTo(200);
        assertThat(getFoodByIdResponse.getBody().getId()).isEqualTo(foodToCreate.getId());
        assertThat(getFoodByIdResponse.getBody().getName()).isEqualTo("food1");
        assertThat(getFoodByIdResponse.getBody().getPoints()).isEqualTo(11);
        assertThat(getFoodByIdResponse.getBody().getDescription()).isEqualTo("description 1");
    }

    @Test
    void getExistingFoods() {
        Food food1 = new Food(null, "food1", 11, "description 1");
        Food food2 = new Food(null, "food2", 22, "description 2");
        Collection<Food> foodsToCreate = List.of(food1, food2);
        mongoTemplate.insertAll(foodsToCreate);

        ResponseEntity<Foods> getAllFoodsResponse = testRestTemplate.getForEntity(urlGetAllFoods, Foods.class);
        assertThat(getAllFoodsResponse.getStatusCode().value()).isEqualTo(200);

        assertThat(getAllFoodsResponse.getBody().getFoods().size()).isEqualTo(foodsToCreate.size());
        assertThat(getAllFoodsResponse.getBody().getFoods()).isEqualTo(foodsToCreate);
    }

    @Test
    void createFoodsAndReadThem() throws MalformedURLException {
        URI urlSave = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(HOST)
                .port(port)
                .path("/store/food")
                .build().toUri();

        ResponseEntity<Food> created = testRestTemplate.postForEntity(urlSave, new Food(null, "food name", 2.5, "some desc"), Food.class);
        assertThat(created.getStatusCode().value()).isEqualTo(201);
        assertThat(created.getBody().getId()).isNotNull();
        assertThat(created.getBody().getName()).isEqualTo("food name");
        assertThat(created.getBody().getPoints()).isEqualTo(2.5);
        assertThat(created.getBody().getDescription()).isEqualTo("some desc");


//
        ResponseEntity<Foods> response = testRestTemplate.getForEntity(urlGetAllFoods, Foods.class);
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody().getFoods().size()).isEqualTo(1);
        assertThat(response.getBody().getFoods().get(0).getId()).isEqualTo(created.getBody().getId());
        assertThat(response.getBody().getFoods().get(0).getName()).isEqualTo("food name");
        assertThat(response.getBody().getFoods().get(0).getPoints()).isEqualTo(2.5);
        assertThat(response.getBody().getFoods().get(0).getDescription()).isEqualTo("some desc");
    }


    private void removeAllFromCollection() {
        mongoTemplate.remove(new Query(), COLLECTION_NAME);
    }
}