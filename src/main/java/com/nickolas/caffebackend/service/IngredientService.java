package com.nickolas.caffebackend.service;

import com.nickolas.caffebackend.model.Ingredient;
import com.nickolas.caffebackend.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервіс для керування інгредієнтами.
 * Забезпечує CRUD-операції над інгредієнтами.
 */
@Service
public class IngredientService {
    @Autowired
    private IngredientRepository ingredientRepository;

    /**
     * Отримує список усіх інгредієнтів.
     *
     * @return список інгредієнтів
     */
    public List<Ingredient> getAllIngredients() {
        return ingredientRepository.findAll();
    }

    /**
     * Створює новий інгредієнт.
     *
     * @param ingredient новий інгредієнт
     * @return збережений інгредієнт
     */
    public Ingredient createIngredient(Ingredient ingredient) {
        return ingredientRepository.save(ingredient);
    }

    /**
     * Оновлює існуючий інгредієнт за його ID.
     *
     * @param id        ідентифікатор інгредієнта
     * @param updated   оновлені дані інгредієнта
     * @return оновлений інгредієнт
     * @throws RuntimeException якщо інгредієнт не знайдено
     */
    public Ingredient updateIngredient(Long id, Ingredient updated) {
        Ingredient existing = ingredientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingredient not found"));
        existing.setName(updated.getName());
        existing.setQuantity(updated.getQuantity());
        return ingredientRepository.save(existing);
    }

    /**
     * Видаляє інгредієнт за його ID.
     *
     * @param id ідентифікатор інгредієнта
     * @throws RuntimeException якщо інгредієнт не знайдено
     */
    public void deleteIngredient(Long id) {
        if (!ingredientRepository.existsById(id)) {
            throw new RuntimeException("Ingredient not found");
        }
        ingredientRepository.deleteById(id);
    }

    /**
     * Повертає інгредієнт за його назвою (без урахування регістру).
     *
     * @param name назва інгредієнта
     * @return знайдений інгредієнт
     * @throws RuntimeException якщо інгредієнт не знайдено
     */
    public Ingredient getByName(String name) {
        return ingredientRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new RuntimeException("Ingredient with name " + name + " not found"));
    }
}
