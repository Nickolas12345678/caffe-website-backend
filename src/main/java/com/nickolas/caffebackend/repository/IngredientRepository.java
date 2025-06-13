package com.nickolas.caffebackend.repository;

import com.nickolas.caffebackend.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторій для роботи з інгредієнтами {@link com.nickolas.caffebackend.model.Ingredient}.
 */
@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    /**
     * Пошук інгредієнта за назвою без урахування регістру.
     */
    Optional<Ingredient> findByNameIgnoreCase(String name);
}
