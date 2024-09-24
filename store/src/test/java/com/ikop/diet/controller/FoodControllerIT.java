package com.ikop.diet.controller;

import com.ikop.diet.mapper.FoodMapper;
import com.ikop.diet.model.*;
import com.ikop.diet.repository.FoodRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.CollectionAssert.assertThatCollection;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


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

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private FoodMapper foodMapper;

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


        Food foodToCreate = new Food(null, "food1", 11d, "description 1");
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
    void getFoodByIdForNoneExistingId() {
        String idPath = "/store/food/FOOD_ID";


        Food foodToCreate = new Food(null, "food1", 11d, "description 1");
        mongoTemplate.insert(foodToCreate);
        idPath = idPath.replace("FOOD_ID", "12345567664");
        URI urlGetFoodById = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(HOST)
                .port(port)
                .path(idPath)
                .build().toUri();

        ResponseEntity<Food> getFoodByIdResponse = testRestTemplate.getForEntity(urlGetFoodById, Food.class);

        assertThat(getFoodByIdResponse.getStatusCode().value()).isEqualTo(404);
    }

    @Test
    void getExistingFoods() {
        Food food1 = new Food(null, "food1", 11d, "description 1");
        Food food2 = new Food(null, "food2", 22d, "description 2");
        Collection<Food> foodsToCreate = List.of(food1, food2);
        mongoTemplate.insertAll(foodsToCreate);

        ResponseEntity<Foods> getAllFoodsResponse = testRestTemplate.getForEntity(urlGetAllFoods, Foods.class);
        assertThat(getAllFoodsResponse.getStatusCode().value()).isEqualTo(200);

        assertThat(getAllFoodsResponse.getBody().getFoods().size()).isEqualTo(foodsToCreate.size());
        assertThatCollection(getAllFoodsResponse.getBody().getFoods())
                .containsExactlyInAnyOrderElementsOf(foodsToCreate.stream().map(food -> foodMapper.foodToFoodDto(food)).toList());

    }

    @Test
    void createFoodsAndReadThem() {
        URI urlSave = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(HOST)
                .port(port)
                .path("/store/food")
                .build().toUri();

        ResponseEntity<FoodDTO> created = testRestTemplate.postForEntity(urlSave, new FoodCreateDTO("food name", 2.5, "some desc"), FoodDTO.class);
        assertThat(created.getStatusCode().value()).isEqualTo(201);
        assertThat(created.getBody().getId()).isNotNull();
        assertThat(created.getBody().getName()).isEqualTo("food name");
        assertThat(created.getBody().getPoints()).isEqualTo(2.5);
        assertThat(created.getBody().getDescription()).isEqualTo("some desc");


        ResponseEntity<Foods> response = testRestTemplate.getForEntity(urlGetAllFoods, Foods.class);
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody().getFoods().size()).isEqualTo(1);
        assertThat(response.getBody().getFoods().get(0).getId()).isEqualTo(created.getBody().getId());
        assertThat(response.getBody().getFoods().get(0).getName()).isEqualTo("food name");
        assertThat(response.getBody().getFoods().get(0).getPoints()).isEqualTo(2.5);
        assertThat(response.getBody().getFoods().get(0).getDescription()).isEqualTo("some desc");
    }

    @Test
    void searchForFood() {
        Food food1 = new Food(null, "food1", 11d, "description 1");
        Food food2 = new Food(null, "food2", 22d, "description 2");
        Food food3 = new Food(null, "food3", 22d, "description 3");
        Collection<Food> foodsToCreate = List.of(food1, food2, food3);
        mongoTemplate.insertAll(foodsToCreate);

        String searchPathOrg = "/store/food/search/TEXT_TO_SEARCH";

        String searchPath = searchPathOrg.replace("TEXT_TO_SEARCH", "food1");
        URI urlSearchForFood = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(HOST)
                .port(port)
                .path(searchPath)
                .build().toUri();


        ResponseEntity<Food[]> getFoodByIdResponse = testRestTemplate.getForEntity(urlSearchForFood, Food[].class);
        assertThat(getFoodByIdResponse.getStatusCode().value()).isEqualTo(200);
        assertThat(Arrays.stream(getFoodByIdResponse.getBody()).count()).isEqualTo(1);
        assertThat(getFoodByIdResponse.getBody()[0].getId()).isEqualTo(food1.getId());


        searchPath = searchPathOrg.replace("TEXT_TO_SEARCH", "description");
        urlSearchForFood = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(HOST)
                .port(port)
                .path(searchPath)
                .build().toUri();

        getFoodByIdResponse = testRestTemplate.getForEntity(urlSearchForFood, Food[].class);
        assertThat(getFoodByIdResponse.getStatusCode().value()).isEqualTo(200);
        assertThat(Arrays.stream(getFoodByIdResponse.getBody()).count()).isEqualTo(3);
        List<String> allFoundsIds = Arrays.stream(getFoodByIdResponse.getBody()).map(Food::getId).toList();
        List<String> expectedIds = foodsToCreate.stream().map(Food::getId).toList();

        // all 3 foods need to be returned
        assertThatCollection(allFoundsIds).containsExactlyInAnyOrderElementsOf(expectedIds);
    }

    @Test
    void testUpdateFood() throws JsonProcessingException {
        Food food1 = new Food(null, "food1", 11d, "description 1");
        mongoTemplate.insert(food1);

        String idPath = "/store/food/FOOD_ID";
        idPath = idPath.replace("FOOD_ID", food1.getId());
        URI urlGetFoodById = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(HOST)
                .port(port)
                .path(idPath)
                .build().toUri();

        FoodUpdateDTO foodToUpdate = new FoodUpdateDTO(food1.getId(), "food 1 after update", 13d, "decription after update");
        String requestBody = new ObjectMapper().writeValueAsString(foodToUpdate);
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Void> responseEntity = testRestTemplate.exchange(urlGetFoodById, HttpMethod.PUT, httpEntity, Void.class);


        assertThat(responseEntity.getBody()).isNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));

        Food foodAfterUpdate = mongoTemplate.findById(food1.getId(), Food.class);
        assertThat(foodAfterUpdate).isNotNull();
        assertThat(foodAfterUpdate).isEqualTo(foodMapper.foodUpdateToFood(foodToUpdate));
    }

    @Test
    void testUpdateFoodWithIncorrectIdInPath() throws JsonProcessingException {
        Food food1 = new Food(null, "food1", 11d, "description 1");
        mongoTemplate.insert(food1);

        String idPath = "/store/food/FOOD_ID";
        idPath = idPath.replace("FOOD_ID", "123123123");
        URI urlGetFoodById = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(HOST)
                .port(port)
                .path(idPath)
                .build().toUri();

        food1.setName("food 1 after update");
        food1.setDescription("decription after update");
        food1.setPoints(13d);
        String requestBody = new ObjectMapper().writeValueAsString(food1);
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        HttpEntity<String> httpEntity = new HttpEntity<String>(requestBody, headers);

        ResponseEntity<Void> responseEntity = testRestTemplate.exchange(urlGetFoodById, HttpMethod.PUT, httpEntity, Void.class);

        assertThat(responseEntity.getBody()).isNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(400));

        // check nothing was changed in DB
        Optional<Food> optResponseAfterUpdateDbEntity = foodRepository.findById(food1.getId());
        assertThat(optResponseAfterUpdateDbEntity).isPresent();
        Food foodEntryAfterUpdate = optResponseAfterUpdateDbEntity.get();
        assertThat(foodEntryAfterUpdate).isNotEqualTo(food1);
        assertThat(foodEntryAfterUpdate.getDescription()).isEqualTo("description 1");
        assertThat(foodEntryAfterUpdate.getPoints()).isEqualTo(11);
        assertThat(foodEntryAfterUpdate.getName()).isEqualTo("food1");
    }


    @Test
    void testUpdateFoodForNonExistingId() throws JsonProcessingException {
        Food food1 = new Food(null, "food1", 11d, "description 1");
        mongoTemplate.insert(food1);

        FoodUpdateDTO foodNonExisting = new FoodUpdateDTO("666", "food2", 11d, "description 2");

        String idPath = "/store/food/666";
        URI urlGetFoodById = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(HOST)
                .port(port)
                .path(idPath)
                .build().toUri();

        String requestBody = new ObjectMapper().writeValueAsString(foodNonExisting);
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        HttpEntity<String> httpEntity = new HttpEntity<String>(requestBody, headers);

        ResponseEntity<Void> responseEntity = testRestTemplate.exchange(urlGetFoodById, HttpMethod.PUT, httpEntity, Void.class);

        assertThat(responseEntity.getBody()).isNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(404));
    }

    @Test
    void testCreateInvalidFoodShouldFailByValidationErrors() {
        URI urlSave = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(HOST)
                .port(port)
                .path("/store/food")
                .build().toUri();

        FoodCreateDTO foodCreateDTO = new FoodCreateDTO(null, -3.5d, "some desc");
        ResponseEntity<String> created = testRestTemplate.postForEntity(urlSave, foodCreateDTO, String.class);
        assertThat(created.getStatusCode().value()).isEqualTo(400);
        assertThat(created.getBody()).contains("name cannot be empty");
        assertThat(created.getBody()).contains("minimal value of points is 0.1");
    }


    @Test
    void testUpdateInvalidFoodShouldFailByValidationErrors() throws JsonProcessingException {
        Food food1 = new Food(null, "food1", 11d, "description 1");
        mongoTemplate.insert(food1);

        String idPath = "/store/food/FOOD_ID";
        idPath = idPath.replace("FOOD_ID", food1.getId());
        URI urlGetFoodById = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(HOST)
                .port(port)
                .path(idPath)
                .build().toUri();

        FoodUpdateDTO foodToUpdate = new FoodUpdateDTO(food1.getId(), null, -3d, "decription after update");
        String requestBody = new ObjectMapper().writeValueAsString(foodToUpdate);
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> responseEntity = testRestTemplate.exchange(urlGetFoodById, HttpMethod.PUT, httpEntity, String.class);

        assertThat(responseEntity.getStatusCode().value()).isEqualTo(400);
        assertThat(responseEntity.getBody()).contains("name cannot be empty");
        assertThat(responseEntity.getBody()).contains("minimal value of points is 0.1");
    }

    private void removeAllFromCollection() {
        mongoTemplate.remove(new Query(), COLLECTION_NAME);
    }
}