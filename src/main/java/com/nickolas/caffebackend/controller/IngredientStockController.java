package com.nickolas.caffebackend.controller;

import com.nickolas.caffebackend.model.IngredientStock;
import com.nickolas.caffebackend.request.IngredientStockRequest;
import com.nickolas.caffebackend.service.IngredientStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST-контролер для управління запасами інгредієнтів.
 * Надає ендпоінти для отримання, створення, оновлення та видалення інформації про запаси інгредієнтів.
 */
@RestController
@RequestMapping("/api/ingredient-stocks")
public class IngredientStockController {
    private final IngredientStockService ingredientStockService;

    /**
     * Конструктор для впровадження сервісу IngredientStockService.
     *
     * @param ingredientStockService сервіс для роботи з запасами інгредієнтів
     */
    @Autowired
    public IngredientStockController(IngredientStockService ingredientStockService) {
        this.ingredientStockService = ingredientStockService;
    }

    /**
     * Отримати список усіх запасів інгредієнтів.
     *
     * @return список об'єктів IngredientStock
     */
    @GetMapping
    public List<IngredientStock> getAllIngredientStocks() {
        return ingredientStockService.getAllIngredientStocks();
    }

    /**
     * Отримати інформацію про конкретний запас інгредієнта за його ідентифікатором.
     *
     * @param id ідентифікатор запасу інгредієнта
     * @return об'єкт IngredientStock або статус 404, якщо не знайдено
     */
    @GetMapping("/{id}")
    public ResponseEntity<IngredientStock> getIngredientStockById(@PathVariable("id") Long id) {
        return ingredientStockService.getIngredientStockById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    /**
     * Створити новий запис про запас інгредієнта.
     *
     * @param request об'єкт запиту з даними про інгредієнт
     * @return створений об'єкт IngredientStock зі статусом 201
     */
    @PostMapping
    public ResponseEntity<IngredientStock> createIngredientStock(@RequestBody IngredientStockRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ingredientStockService.createIngredientStock(request));
    }

    /**
     * Оновити наявний запис про запас інгредієнта.
     *
     * @param id      ідентифікатор запасу, який потрібно оновити
     * @param request об'єкт запиту з оновленими даними
     * @return оновлений об'єкт IngredientStock або статус 404, якщо не знайдено
     */
    @PutMapping("/{id}")
    public ResponseEntity<IngredientStock> updateIngredientStock(@PathVariable("id") Long id,
                                                                 @RequestBody IngredientStockRequest request) {
        try {
            return ResponseEntity.ok(ingredientStockService.updateIngredientStock(id, request));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Видалити запис про запас інгредієнта за його ідентифікатором.
     *
     * @param id ідентифікатор запису, який потрібно видалити
     * @return статус 204, якщо успішно, або 404, якщо запис не знайдено
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIngredientStock(@PathVariable("id") Long id) {
        try {
            ingredientStockService.deleteIngredientStock(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
