package com.nickolas.caffebackend.service;

import com.nickolas.caffebackend.model.IngredientStock;
import com.nickolas.caffebackend.repository.IngredientStockRepository;
import com.nickolas.caffebackend.request.IngredientStockRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Сервіс для керування запасами інгредієнтів на складі.
 * Надає CRUD-операції та пошук за назвою.
 */
@Service
public class IngredientStockService {
    private final IngredientStockRepository ingredientStockRepository;

    @Autowired
    public IngredientStockService(IngredientStockRepository ingredientStockRepository) {
        this.ingredientStockRepository = ingredientStockRepository;
    }

    /**
     * Повертає всі запаси інгредієнтів.
     *
     * @return список усіх інгредієнтів на складі
     */
    public List<IngredientStock> getAllIngredientStocks() {
        return ingredientStockRepository.findAll();
    }

    /**
     * Повертає інгредієнт зі складу за його ID.
     *
     * @param id ідентифікатор інгредієнта
     * @return Optional з інгредієнтом або порожній, якщо не знайдено
     */
    public Optional<IngredientStock> getIngredientStockById(Long id) {
        return ingredientStockRepository.findById(id);
    }

    /**
     * Створює новий запис про інгредієнт на складі.
     *
     * @param request запит з параметрами інгредієнта
     * @return збережений об'єкт IngredientStock
     */
    public IngredientStock createIngredientStock(IngredientStockRequest request) {
        IngredientStock stock = new IngredientStock();
        stock.setName(request.getName());
        stock.setAvailableQuantity(parseQuantity(request.getAvailableQuantity()));
        stock.setUnit(request.getUnit());
        return ingredientStockRepository.save(stock);
    }

    /**
     * Оновлює дані про існуючий інгредієнт на складі.
     *
     * @param id      ідентифікатор інгредієнта
     * @param request нові дані
     * @return оновлений об'єкт IngredientStock
     * @throws RuntimeException якщо інгредієнт не знайдено
     */
    public IngredientStock updateIngredientStock(Long id, IngredientStockRequest request) {
        IngredientStock existing = ingredientStockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("IngredientStock not found"));
        existing.setName(request.getName());
        existing.setAvailableQuantity(parseQuantity(request.getAvailableQuantity()));
        existing.setUnit(request.getUnit());
        return ingredientStockRepository.save(existing);
    }

    /**
     * Видаляє інгредієнт зі складу за його ID.
     *
     * @param id ідентифікатор інгредієнта
     * @throws RuntimeException якщо інгредієнт не знайдено
     */
    public void deleteIngredientStock(Long id) {
        if (!ingredientStockRepository.existsById(id)) {
            throw new RuntimeException("IngredientStock not found");
        }
        ingredientStockRepository.deleteById(id);
    }

    /**
     * Перетворює рядок з кількістю у формат типу double.
     *
     * @param quantityStr вхідний рядок (наприклад, "1.5 кг" або "1,5 л")
     * @return кількість як число типу double
     * @throws RuntimeException якщо формат неправильний
     */
    private double parseQuantity(String quantityStr) {
        try {
            String numericPart = quantityStr.replaceAll(",", ".").replaceAll("[^0-9.]", "");
            if (numericPart.isEmpty()) {
                throw new RuntimeException("Неправильний формат кількості: " + quantityStr);
            }
            return Double.parseDouble(numericPart);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Неможливо перетворити '" + quantityStr + "' в число", e);
        }
    }

    /**
     * Пошук інгредієнта на складі за назвою (без урахування регістру).
     *
     * @param name назва інгредієнта
     * @return знайдений об'єкт IngredientStock
     * @throws RuntimeException якщо інгредієнт не знайдено
     */
    public IngredientStock getByName(String name) {
        return ingredientStockRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new RuntimeException("IngredientStock with name " + name + " not found"));
    }
}
