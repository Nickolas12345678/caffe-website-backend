package com.nickolas.caffebackend.controller;

import com.nickolas.caffebackend.model.Category;
import com.nickolas.caffebackend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Контролер для керування категоріями страв.
 * Забезпечує базові CRUD-операції: створення, читання, оновлення та видалення категорій.
 */
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    /**
     * Конструктор із впровадженням залежності {@link CategoryService}.
     *
     * @param categoryService сервіс для роботи з категоріями
     */
    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Отримати всі категорії.
     *
     * @return список усіх об'єктів {@link Category}
     */
    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    /**
     * Отримати категорію за ідентифікатором.
     *
     * @param id ідентифікатор категорії
     * @return об'єкт {@link Category} або статус 404, якщо категорію не знайдено
     */
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable("id") Long id) {
        Optional<Category> category = categoryService.getCategoryById(id);
        return category.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Створити нову категорію.
     *
     * @param category об'єкт {@link Category}, що підлягає створенню
     * @return створена категорія зі статусом 201 Created
     */
    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        Category createdCategory = categoryService.createCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }

    /**
     * Оновити існуючу категорію за її ідентифікатором.
     *
     * @param id       ідентифікатор категорії
     * @param category об'єкт {@link Category} з оновленими даними
     * @return оновлена категорія або статус 404, якщо не знайдено
     */
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable("id") Long id, @RequestBody Category category) {
        try {
            Category updatedCategory = categoryService.updateCategory(id, category);
            return ResponseEntity.ok(updatedCategory);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Видалити категорію за її ідентифікатором.
     *
     * @param id ідентифікатор категорії
     * @return статус 204 No Content або статус 404, якщо категорію не знайдено
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") Long id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
