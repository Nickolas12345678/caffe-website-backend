package com.nickolas.caffebackend.repository;

import com.nickolas.caffebackend.model.Dish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
    Page<Dish> findByCategoryId(Long categoryId, Pageable pageable);
}
