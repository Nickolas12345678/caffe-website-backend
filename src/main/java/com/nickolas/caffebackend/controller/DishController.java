package com.nickolas.caffebackend.controller;


import com.nickolas.caffebackend.model.Dish;
import com.nickolas.caffebackend.request.DishCreateRequest;
import com.nickolas.caffebackend.request.DishUpdateRequest;
import com.nickolas.caffebackend.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST-контролер для керування стравами у кафе.
 * Надає ендпоінти для отримання, створення, оновлення, видалення та фільтрації страв.
 */
@RestController
@RequestMapping("/api/dishes")
public class DishController {
    private final DishService dishService;

    /**
     * Конструктор для автоматичного впровадження сервісу страв.
     *
     * @param dishService сервіс для роботи зі стравами
     */
    @Autowired
    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    /**
     * Отримати сторінку всіх страв.
     *
     * @param page номер сторінки (за замовчуванням 0)
     * @param size розмір сторінки (за замовчуванням 10)
     * @return сторінка страв
     */
    @GetMapping
    public ResponseEntity<Page<Dish>> getAllDishes(@RequestParam(name = "page", defaultValue = "0") int page,
                                                   @RequestParam(name = "size", defaultValue = "10") int size) {
        Page<Dish> dishes = dishService.getAllDishes(PageRequest.of(page, size));
        return ResponseEntity.ok(dishes);
    }

    /**
     * Отримати страви за категорією з можливістю фільтрації за ціною, назвою та сортуванням.
     *
     * @param categoryId ідентифікатор категорії
     * @param page номер сторінки (за замовчуванням 0)
     * @param size розмір сторінки (за замовчуванням 10)
     * @param minPrice мінімальна ціна (необов’язково)
     * @param maxPrice максимальна ціна (необов’язково)
     * @param sortOrder параметр сортування (необов’язково)
     * @param name назва для пошуку (необов’язково)
     * @return сторінка страв, що відповідають фільтру
     */
@GetMapping("/category/{categoryId}")
public ResponseEntity<Page<Dish>> getDishesByCategory(
        @PathVariable("categoryId") Long categoryId,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size,
        @RequestParam(name = "minPrice", required = false) Double minPrice,
        @RequestParam(name = "maxPrice", required = false) Double maxPrice,
        @RequestParam(name = "sort", required = false, defaultValue = "") String sortOrder,  // Параметр для сортування
        @RequestParam(name = "name", required = false) String name) {

    PageRequest pageRequest = PageRequest.of(page, size);

    // Якщо сортування не задано, не застосовуємо сортування
    if (sortOrder.isEmpty()) {
        sortOrder = null;
    }

    Page<Dish> dishes;

    if (minPrice != null || maxPrice != null) {
        dishes = dishService.getDishesByCategoryWithPriceFilter(categoryId, pageRequest, minPrice, maxPrice, sortOrder, name);
    } else {
        dishes = dishService.getDishesByCategory(categoryId, pageRequest, sortOrder, name);
    }

    return ResponseEntity.ok(dishes);
}

    /**
     * Отримати страву за її ідентифікатором.
     *
     * @param id ідентифікатор страви
     * @return відповідь з об’єктом страви або статус 404, якщо не знайдено
     */
    @GetMapping("/{id}")
    public ResponseEntity<Dish> getDishById(@PathVariable("id") Long id) {
        Optional<Dish> dish = dishService.getDishById(id);
        return dish.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Створити нову страву.
     *
     * @param request запит на створення страви
     * @return відповідь зі створеною стравою
     */
    @PostMapping
    public ResponseEntity<Dish> createDish(@RequestBody DishCreateRequest request) {
        Dish createdDish = dishService.createDish(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDish);
    }

    /**
     * Оновити існуючу страву за її ідентифікатором.
     *
     * @param id ідентифікатор страви
     * @param request запит з новими даними страви
     * @return відповідь з оновленою стравою або статус 404, якщо не знайдено
     */
    @PutMapping("/{id}")
    public ResponseEntity<Dish> updateDish(@PathVariable("id") Long id, @RequestBody DishUpdateRequest request) {
        try {
            Dish updatedDish = dishService.updateDish(id, request);
            return ResponseEntity.ok(updatedDish);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Видалити страву за її ідентифікатором.
     *
     * @param id ідентифікатор страви
     * @return статус 204, якщо успішно, або 404, якщо не знайдено
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDish(@PathVariable("id") Long id) {
        try {
            dishService.deleteDish(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
