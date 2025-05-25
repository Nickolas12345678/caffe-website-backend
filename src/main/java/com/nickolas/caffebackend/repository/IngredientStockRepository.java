package com.nickolas.caffebackend.repository;

import com.nickolas.caffebackend.model.IngredientStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IngredientStockRepository extends JpaRepository<IngredientStock, Long> {
    Optional<IngredientStock> findByNameIgnoreCase(String name);
}
