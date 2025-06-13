package com.nickolas.caffebackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

/**
 * Інгредієнт, який використовується у страві.
 * Зв'язаний з відповідним запасом інгредієнтів та стравою.
 */
@Entity
@Table(name = "ingredient")
@Data
public class Ingredient {
/** Унікальний ідентифікатор інгредієнта. */
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

    /** Кількість інгредієнта, необхідна для страви. */
    private String quantity;

    /** Назва інгредієнта. */
    private String name;
//    private int availableQuantity;

    /** Одиниця виміру. */
    private String unit;

    /** Запас інгредієнта на складі. */
    @ManyToOne
    @JoinColumn(name = "ingredient_stock_id")
    private IngredientStock ingredientStock;

    /** Страва, до якої належить інгредієнт. */
    @ManyToOne
    @JoinColumn(name = "dish_id")
    @JsonBackReference
    private Dish dish;
}
