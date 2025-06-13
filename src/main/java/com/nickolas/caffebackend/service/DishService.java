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

/**
 * Сервіс для керування стравами.
 * Забезпечує операції створення, оновлення, видалення, пошуку та фільтрації страв,
 * а також логіку використання інгредієнтів зі складу.
 */
@Service
public class DishService {
private final DishRepository dishRepository;
    private final CategoryRepository categoryRepository;
    private final IngredientStockRepository ingredientStockRepository;
    private IngredientRepository ingredientRepository;

    /**
     * Конструктор з впровадженням залежностей.
     *
     * @param dishRepository              репозиторій страв
     * @param categoryRepository          репозиторій категорій
     * @param ingredientRepository        репозиторій інгредієнтів
     * @param ingredientStockRepository   репозиторій складу інгредієнтів
     */
    @Autowired
    public DishService(DishRepository dishRepository, CategoryRepository categoryRepository, IngredientRepository ingredientRepository, IngredientStockRepository ingredientStockRepository) {
        this.dishRepository = dishRepository;
        this.categoryRepository = categoryRepository;
        this.ingredientStockRepository = ingredientStockRepository;
        this.ingredientRepository = ingredientRepository;
    }

    /**
     * Отримує всі страви.
     *
     * @return список усіх страв
     */
    public List<Dish> getAllDishes() {
        return dishRepository.findAll();
    }

    /**
     * Отримує сторінку страв.
     *
     * @param pageRequest параметри пагінації
     * @return сторінка страв
     */
    public Page<Dish> getAllDishes(PageRequest pageRequest) {
        return dishRepository.findAll(pageRequest);
    }


    /**
     * Повертає страви певної категорії з можливим пошуком за назвою та сортуванням.
     *
     * @param categoryId ідентифікатор категорії
     * @param pageRequest параметри пагінації
     * @param sortOrder параметр сортування (asc/desc)
     * @param name пошуковий рядок по назві страви
     * @return сторінка страв
     */
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

    /**
     * Отримує страви певної категорії з фільтрацією за ціною, пошуком по назві та сортуванням.
     *
     * @param categoryId ідентифікатор категорії
     * @param pageRequest параметри пагінації
     * @param minPrice мінімальна ціна
     * @param maxPrice максимальна ціна
     * @param sortOrder порядок сортування
     * @param name назва для пошуку
     * @return сторінка страв
     */
    public Page<Dish> getDishesByCategoryWithPriceFilter(Long categoryId, PageRequest pageRequest, Double minPrice, Double maxPrice, String sortOrder, String name) {
        if (sortOrder == null || sortOrder.isEmpty()) {
            if (name != null && !name.isEmpty()) {
                return dishRepository.findByCategoryIdAndNameContainingIgnoreCase(categoryId, name, pageRequest);
            } else {
                return dishRepository.findByCategoryIdAndPriceBetween(categoryId, minPrice, maxPrice, pageRequest);
            }
        } else {
            return dishRepository.findByCategoryIdAndPriceBetweenWithSort(categoryId, minPrice, maxPrice, sortOrder, pageRequest);
        }
    }

    /**
     * Повертає страву за її ID.
     *
     * @param id ідентифікатор страви
     * @return Optional зі стравою або порожній, якщо не знайдено
     */
    public Optional<Dish> getDishById(Long id) {
        return dishRepository.findById(id);
    }

    /**
     * Створює нову страву з інгредієнтами та списує їх зі складу.
     *
     * @param request дані для створення страви
     * @return створена страва
     * @throws RuntimeException якщо інгредієнтів недостатньо або інші помилки
     */
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



    /**
     * Оновлює наявну страву та її інгредієнти.
     *
     * @param id ідентифікатор страви
     * @param request дані для оновлення
     * @return оновлена страва
     * @throws RuntimeException якщо страва або категорія не знайдені
     */
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

    /**
     * Парсить числове значення з рядка кількості.
     *
     * @param quantityStr рядок кількості (наприклад, "200 г")
     * @return числове значення
     * @throws RuntimeException якщо не вдалося перетворити
     */
    private int parseQuantity(String quantityStr) {
        try {
            return Integer.parseInt(quantityStr.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Неправильний формат кількості: " + quantityStr);
        }
    }

    /**
     * Видаляє страву за її ID.
     *
     * @param id ідентифікатор страви
     * @throws RuntimeException якщо страву не знайдено
     */
    public void deleteDish(Long id) {
        if (dishRepository.existsById(id)) {
            dishRepository.deleteById(id);
        } else {
            throw new RuntimeException("Dish not found");
        }
    }
}
