package com.nickolas.caffebackend.service;

import com.nickolas.caffebackend.model.Category;
import com.nickolas.caffebackend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Сервіс для керування категоріями страв.
 * Забезпечує створення, отримання, оновлення та видалення категорій.
 */
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    /**
     * Конструктор з впровадженням залежності репозиторію категорій.
     *
     * @param categoryRepository репозиторій для доступу до категорій
     */
    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Створює нову категорію.
     *
     * @param category об'єкт {@link Category} для збереження
     * @return збережена категорія
     */
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    /**
     * Повертає список усіх категорій.
     *
     * @return список категорій
     */
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    /**
     * Шукає категорію за її ідентифікатором.
     *
     * @param id ідентифікатор категорії
     * @return {@link Optional} з категорією або порожній, якщо не знайдено
     */
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    /**
     * Оновлює наявну категорію за ID.
     *
     * @param id ідентифікатор категорії
     * @param category нові дані категорії
     * @return оновлена категорія
     * @throws RuntimeException якщо категорію не знайдено
     */
    public Category updateCategory(Long id, Category category) {
        if (categoryRepository.existsById(id)) {
            category.setId(id);
            return categoryRepository.save(category);
        }
        throw new RuntimeException("Category not found");
    }

    /**
     * Видаляє категорію за її ID.
     *
     * @param id ідентифікатор категорії
     * @throws RuntimeException якщо категорію не знайдено
     */
    public void deleteCategory(Long id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
        } else {
            throw new RuntimeException("Category not found");
        }
    }
}
