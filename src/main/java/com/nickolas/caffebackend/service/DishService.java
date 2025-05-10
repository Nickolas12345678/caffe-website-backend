package com.nickolas.caffebackend.service;

import com.nickolas.caffebackend.model.Category;
import com.nickolas.caffebackend.model.Dish;
import com.nickolas.caffebackend.model.Ingredient;
import com.nickolas.caffebackend.repository.CategoryRepository;
import com.nickolas.caffebackend.repository.DishRepository;
import com.nickolas.caffebackend.request.DishCreateRequest;
import com.nickolas.caffebackend.request.DishUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DishService {
private final DishRepository dishRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public DishService(DishRepository dishRepository, CategoryRepository categoryRepository) {
        this.dishRepository = dishRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<Dish> getAllDishes() {
        return dishRepository.findAll();
    }

    public Page<Dish> getAllDishes(PageRequest pageRequest) {
        return dishRepository.findAll(pageRequest);
    }

    public Page<Dish> getDishesByCategory(Long categoryId, PageRequest pageRequest) {
        return dishRepository.findByCategoryId(categoryId, pageRequest);
    }

    public Optional<Dish> getDishById(Long id) {
        return dishRepository.findById(id);
    }

    public Dish createDish(DishCreateRequest request) {
        Dish dish = new Dish();
        dish.setName(request.getName());
        dish.setDescription(request.getDescription());
        dish.setPrice(request.getPrice());
        dish.setImageUrl(request.getImageUrl());
        dish.setWeight(request.getWeight());
        dish.setPreparationTime(request.getPreparationTime());

        if (request.getIngredients() != null) {
            List<Ingredient> ingredients = request.getIngredients().stream()
                    .map(ing -> {
                        Ingredient ingredient = new Ingredient();
                        ingredient.setName(ing.getName());
                        ingredient.setQuantity(ing.getQuantity());
                        ingredient.setDish(dish);
                        return ingredient;
                    })
                    .toList();
            dish.setIngredients(ingredients);
        }

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found with id: " + request.getCategoryId()));
            dish.setCategory(category);
        }

        return dishRepository.save(dish);
    }

    public Dish updateDish(Long id, DishUpdateRequest request) {
        return dishRepository.findById(id).map(existingDish -> {
            existingDish.setName(request.getName());
            existingDish.setDescription(request.getDescription());
            existingDish.setPrice(request.getPrice());
            existingDish.setImageUrl(request.getImageUrl());
            existingDish.setWeight(request.getWeight());
            existingDish.setPreparationTime(request.getPreparationTime());

            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            existingDish.setCategory(category);

            // Update ingredients
            existingDish.getIngredients().clear();
            if (request.getIngredients() != null) {
                List<Ingredient> updatedIngredients = request.getIngredients().stream()
                        .map(ing -> {
                            Ingredient ingredient = new Ingredient();
                            ingredient.setName(ing.getName());
                            ingredient.setQuantity(ing.getQuantity());
                            ingredient.setDish(existingDish);
                            return ingredient;
                        })
                        .toList();
                existingDish.getIngredients().addAll(updatedIngredients);
            }

            return dishRepository.save(existingDish);
        }).orElseThrow(() -> new RuntimeException("Dish not found"));
    }

    public void deleteDish(Long id) {
        if (dishRepository.existsById(id)) {
            dishRepository.deleteById(id);
        } else {
            throw new RuntimeException("Dish not found");
        }
    }
}
