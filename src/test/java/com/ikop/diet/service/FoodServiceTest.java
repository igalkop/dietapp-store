package com.ikop.diet.service;

import com.ikop.diet.mapper.FoodMapper;
import com.ikop.diet.model.Food;
import com.ikop.diet.model.FoodToCreate;
import com.ikop.diet.repository.FoodRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FoodServiceTest {

    @Mock
    private FoodRepository foodRepository;

    @Mock
    private FoodMapper foodMapper;

    @InjectMocks
    FoodService foodService;

    @Test
    void testSave() {
        FoodToCreate foodToCreate = new FoodToCreate("food 1", 2d, "desc 1");
        Food food = new Food(null, "food1", 2d, "desc 1");
        Food foodCreated = new Food("123", "food 1", 2d, "desc 1");

        when(foodMapper.foodCreateToFood(foodToCreate)).thenReturn(food);
        when(foodRepository.save(food)).thenReturn(foodCreated);

        food = foodService.saveFood(foodToCreate);

        assertThat(food).isEqualTo(foodCreated);
    }

    @Test
    void testGetAllFoods() {
        List<Food> allPersistedFoods = List.of(
                new Food("123", "food 1", 1d, "desc 1"),
                new Food("456", "food 2", 2d, "desc 2")
        );
        when(foodRepository.findAll()).thenReturn(allPersistedFoods);

        List<Food> allFoods = foodService.getAllFoods();

        assertThatCollection(allFoods).containsExactlyInAnyOrderElementsOf(allPersistedFoods);
    }

    @Test
    void testUpdateFood() {
        Food foodToUpdate = new Food("123", "food1", 2.5d, "desc1");
        when(foodRepository.existsById("123")).thenReturn(true);

        foodService.updateFood("123", foodToUpdate);

        verify(foodRepository, times(1)).save(foodToUpdate);
    }

    @Test
    void testUpdateFoodWhenEntityIdMimatch() {
        Food foodToUpdate = new Food("123", "food1", 2.5d, "desc1");

        Throwable thrown = catchThrowable(() -> {
            foodService.updateFood("456", foodToUpdate);
        });

        assertThat(thrown).isInstanceOf(FoodIdNotMatchForUpdateException.class);
        assertThat(thrown).hasMessageContaining("Food ID in path 456 does not match food ID in entity to update 123");
        verify(foodRepository, times(0)).save(foodToUpdate);
    }


    @Test
    void testUpdateFoodWhenEntityDoesNotExists() {
        Food foodToUpdate = new Food("123", "food1", 2.5d, "desc1");
        when(foodRepository.existsById("123")).thenReturn(false);


        Throwable thrown = catchThrowable(() -> {
            foodService.updateFood("123", foodToUpdate);
        });

        assertThat(thrown).isInstanceOf(FoodNotFoundException.class);
        assertThat(thrown).hasMessageContaining("Cannot find food with id 123");
        verify(foodRepository, times(0)).save(foodToUpdate);
    }


    @Test
    void testFindById() {
        Optional<Food> foodFromDb = Optional.of(new Food("123", "food1", 2.6d, "desc1"));
        when(foodRepository.findById("123")).thenReturn(foodFromDb);

        Food found = foodService.findById("123");

        assertThat(found).isEqualTo(foodFromDb.get());
    }

    @Test
    void testFindByIdWhenEntityNotFound() {
        Optional<Food> foodFromDb = Optional.empty();
        when(foodRepository.findById("123")).thenReturn(foodFromDb);

        Throwable thrown = catchThrowable(() -> {
            foodService.findById("123");
        });

        assertThat(thrown).isInstanceOf(FoodNotFoundException.class);
        assertThat(thrown).hasMessageContaining("Cannot find food with id 123");
    }

    @Test
    void testSearchForFood() {
        List<Food> foodsFoundInSearch = List.of(
                new Food("123", "food1", 1.5d, "desc1"),
                new Food("456", "food2", 2.5d, "desc2")
        );

        when(foodRepository.searchByText("fo")).thenReturn(foodsFoundInSearch);

        List<Food> foundFoods = foodService.searchForFood("fo");

        assertThatCollection(foundFoods).containsExactlyInAnyOrderElementsOf(foodsFoundInSearch);

    }
}