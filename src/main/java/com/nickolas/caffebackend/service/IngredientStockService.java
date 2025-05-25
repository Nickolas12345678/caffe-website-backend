package com.nickolas.caffebackend.service;

import com.nickolas.caffebackend.model.IngredientStock;
import com.nickolas.caffebackend.repository.IngredientStockRepository;
import com.nickolas.caffebackend.request.IngredientStockRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IngredientStockService {
    private final IngredientStockRepository ingredientStockRepository;

    @Autowired
    public IngredientStockService(IngredientStockRepository ingredientStockRepository) {
        this.ingredientStockRepository = ingredientStockRepository;
    }

    public List<IngredientStock> getAllIngredientStocks() {
        return ingredientStockRepository.findAll();
    }

    public Optional<IngredientStock> getIngredientStockById(Long id) {
        return ingredientStockRepository.findById(id);
    }


    public IngredientStock createIngredientStock(IngredientStockRequest request) {
        IngredientStock stock = new IngredientStock();
        stock.setName(request.getName());
        stock.setAvailableQuantity(parseQuantity(request.getAvailableQuantity()));
        stock.setUnit(request.getUnit());
        return ingredientStockRepository.save(stock);
    }

    public IngredientStock updateIngredientStock(Long id, IngredientStockRequest request) {
        IngredientStock existing = ingredientStockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("IngredientStock not found"));
        existing.setName(request.getName());
        existing.setAvailableQuantity(parseQuantity(request.getAvailableQuantity()));
        existing.setUnit(request.getUnit());
        return ingredientStockRepository.save(existing);
    }


    public void deleteIngredientStock(Long id) {
        if (!ingredientStockRepository.existsById(id)) {
            throw new RuntimeException("IngredientStock not found");
        }
        ingredientStockRepository.deleteById(id);
    }

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


    public IngredientStock getByName(String name) {
        return ingredientStockRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new RuntimeException("IngredientStock with name " + name + " not found"));
    }
}
