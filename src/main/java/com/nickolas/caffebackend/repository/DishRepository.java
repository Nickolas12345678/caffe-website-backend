package com.nickolas.caffebackend.repository;

import com.nickolas.caffebackend.model.Dish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Репозиторій для роботи зі стравами {@link com.nickolas.caffebackend.model.Dish}.
 * Підтримує пошук за категорією, ціною, назвою та сортування.
 */
@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {

    /**
     * Знаходить сторінку страв для вказаної категорії.
     */
    Page<Dish> findByCategoryId(Long categoryId, Pageable pageable);

    /**
     * Фільтрує страви за категорією та діапазоном цін.
     */
    @Query("SELECT d FROM Dish d " +
            "WHERE d.category.id = :categoryId " +
            "AND (:minPrice IS NULL OR d.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR d.price <= :maxPrice) " +
            "ORDER BY d.price " +
            "ASC")
    Page<Dish> findByCategoryIdAndPriceBetween(
            @Param("categoryId") Long categoryId,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            Pageable pageable);

    /**
     * Фільтрує страви за категорією, діапазоном цін та сортує по ціні.
     */
    @Query("SELECT d FROM Dish d " +
            "WHERE d.category.id = :categoryId " +
            "AND (:minPrice IS NULL OR d.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR d.price <= :maxPrice) " +
            "ORDER BY CASE WHEN :sortOrder = 'asc' THEN d.price END ASC, " +
            "CASE WHEN :sortOrder = 'desc' THEN d.price END DESC")
    Page<Dish> findByCategoryIdAndPriceBetweenWithSort(
            @Param("categoryId") Long categoryId,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("sortOrder") String sortOrder,
            Pageable pageable);

    /**
     * Пошук страв по частковому збігу назви (ігноруючи регістр).
     */
    Page<Dish> findByCategoryIdAndNameContainingIgnoreCase(
            @Param("categoryId") Long categoryId,
            @Param("name") String name,
            Pageable pageable);
}
