package com.nickolas.caffebackend.service;

import com.nickolas.caffebackend.model.Ingredient;
import com.nickolas.caffebackend.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientService {
    @Autowired
    private IngredientRepository ingredientRepository;

    public List<Ingredient> getAllIngredients() {
        return ingredientRepository.findAll();
    }

    public Ingredient createIngredient(Ingredient ingredient) {
        return ingredientRepository.save(ingredient);
    }

    public Ingredient updateIngredient(Long id, Ingredient updated) {
        Ingredient existing = ingredientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingredient not found"));
        existing.setName(updated.getName());
        existing.setQuantity(updated.getQuantity());
        return ingredientRepository.save(existing);
    }

    public void deleteIngredient(Long id) {
        if (!ingredientRepository.existsById(id)) {
            throw new RuntimeException("Ingredient not found");
        }
        ingredientRepository.deleteById(id);
    }

    public Ingredient getByName(String name) {
        return ingredientRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new RuntimeException("Ingredient with name " + name + " not found"));
    }
}
