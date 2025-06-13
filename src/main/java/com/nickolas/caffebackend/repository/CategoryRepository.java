package com.nickolas.caffebackend.repository;

import com.nickolas.caffebackend.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторій для роботи з категоріями страв {@link com.nickolas.caffebackend.model.Category}.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
