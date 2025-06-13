package com.nickolas.caffebackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Запас інгредієнтів на складі.
 * Відображає наявну кількість інгредієнтів та одиницю виміру.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ingredient_stock")
public class IngredientStock {

    /** Унікальний ідентифікатор запису. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Назва інгредієнта на складі. */
    private String name;

    /** Доступна кількість інгредієнта. */
    private double availableQuantity;

    /** Одиниця виміру. */
    private String unit;
}
