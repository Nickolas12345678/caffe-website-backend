package com.nickolas.caffebackend.service;

import com.nickolas.caffebackend.model.Category;
import com.nickolas.caffebackend.model.Dish;
import com.nickolas.caffebackend.model.Ingredient;
import com.nickolas.caffebackend.model.IngredientStock;
import com.nickolas.caffebackend.repository.CategoryRepository;
import com.nickolas.caffebackend.repository.DishRepository;
import com.nickolas.caffebackend.repository.IngredientRepository;
import com.nickolas.caffebackend.repository.IngredientStockRepository;
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
    private final IngredientStockRepository ingredientStockRepository;
    private IngredientRepository ingredientRepository;

    @Autowired
    public DishService(DishRepository dishRepository, CategoryRepository categoryRepository, IngredientRepository ingredientRepository, IngredientStockRepository ingredientStockRepository) {
        this.dishRepository = dishRepository;
        this.categoryRepository = categoryRepository;
        this.ingredientStockRepository = ingredientStockRepository;
        this.ingredientRepository = ingredientRepository;
    }

    public List<Dish> getAllDishes() {
        return dishRepository.findAll();
    }

    public Page<Dish> getAllDishes(PageRequest pageRequest) {
        return dishRepository.findAll(pageRequest);
    }


//public Page<Dish> getDishesByCategory(Long categoryId, PageRequest pageRequest, Double minPrice, Double maxPrice, String sortOrder) {
//    if (minPrice != null || maxPrice != null) {
//        return dishRepository.findByCategoryIdAndPriceBetweenWithSort(categoryId, minPrice, maxPrice, sortOrder, pageRequest);
//    } else {
//        return dishRepository.findByCategoryId(categoryId, pageRequest);
//    }
//}
// Оновлена версія для збереження сортування та фільтрування
public Page<Dish> getDishesByCategory(Long categoryId, PageRequest pageRequest, String sortOrder, String name) {
    if (sortOrder == null || sortOrder.isEmpty()) {
        // Якщо сортування не вказано, сортуємо за замовчуванням, якщо необхідно
        if (name != null && !name.isEmpty()) {
            return dishRepository.findByCategoryIdAndNameContainingIgnoreCase(categoryId, name, pageRequest);
        } else {
            return dishRepository.findByCategoryId(categoryId, pageRequest);
        }
    } else {
        // Якщо сортування вказано
        return dishRepository.findByCategoryIdAndPriceBetweenWithSort(categoryId, null, null, sortOrder, pageRequest);
    }
}

    // Оновлене з фільтром по ціні
    public Page<Dish> getDishesByCategoryWithPriceFilter(Long categoryId, PageRequest pageRequest, Double minPrice, Double maxPrice, String sortOrder, String name) {
        if (sortOrder == null || sortOrder.isEmpty()) {
            // Якщо сортування не вказано, сортуємо за замовчуванням
            if (name != null && !name.isEmpty()) {
                return dishRepository.findByCategoryIdAndNameContainingIgnoreCase(categoryId, name, pageRequest);
            } else {
                return dishRepository.findByCategoryIdAndPriceBetween(categoryId, minPrice, maxPrice, pageRequest);
            }
        } else {
            // Якщо сортування вказано
            return dishRepository.findByCategoryIdAndPriceBetweenWithSort(categoryId, minPrice, maxPrice, sortOrder, pageRequest);
        }
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
            List<Ingredient> usedIngredients = request.getIngredients().stream().map(reqIng -> {
                IngredientStock stock = ingredientStockRepository.findByNameIgnoreCase(reqIng.getName())
                        .orElseThrow(() -> new RuntimeException("Інгредієнт '" + reqIng.getName() + "' не знайдено на складі"));

                int quantityToUse = parseQuantity(reqIng.getQuantity());
                if (stock.getAvailableQuantity() < quantityToUse) {
                    throw new RuntimeException("Недостатньо інгредієнта '" + reqIng.getName() + "'. Доступно: "
                            + stock.getAvailableQuantity() + ", потрібно: " + quantityToUse);
                }

                stock.setAvailableQuantity(stock.getAvailableQuantity() - quantityToUse);
                ingredientStockRepository.save(stock);

                Ingredient newIngredient = new Ingredient();
                newIngredient.setName(reqIng.getName());
                newIngredient.setQuantity(reqIng.getQuantity());
                newIngredient.setUnit(stock.getUnit());
                newIngredient.setDish(dish);
                return newIngredient;
            }).toList();

            dish.setIngredients(usedIngredients);
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

            existingDish.getIngredients().clear();
            if (request.getIngredients() != null) {
                List<Ingredient> updatedIngredients = request.getIngredients().stream()
                        .map(ing -> {
                            Ingredient ingredient = new Ingredient();
                            ingredient.setName(ing.getName());
                            ingredient.setQuantity(ing.getQuantity());
                            ingredient.setDish(existingDish);
                            ingredientStockRepository.findByNameIgnoreCase(ing.getName())
                                    .ifPresent(stock -> ingredient.setUnit(stock.getUnit()));
                            return ingredient;
                        })
                        .toList();
                existingDish.getIngredients().addAll(updatedIngredients);
            }



            return dishRepository.save(existingDish);
        }).orElseThrow(() -> new RuntimeException("Dish not found"));
    }


    private int parseQuantity(String quantityStr) {
        try {
            return Integer.parseInt(quantityStr.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Неправильний формат кількості: " + quantityStr);
        }
    }


    public void deleteDish(Long id) {
        if (dishRepository.existsById(id)) {
            dishRepository.deleteById(id);
        } else {
            throw new RuntimeException("Dish not found");
        }
    }
}
