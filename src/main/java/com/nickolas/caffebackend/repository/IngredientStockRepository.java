package com.nickolas.caffebackend.repository;

import com.nickolas.caffebackend.model.IngredientStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Репозиторій для складу інгредієнтів {@link com.nickolas.caffebackend.model.IngredientStock}.
 */
public interface IngredientStockRepository extends JpaRepository<IngredientStock, Long> {

    /**
     * Знаходить інгредієнт на складі за назвою без урахування регістру.
     */
    Optional<IngredientStock> findByNameIgnoreCase(String name);
}
