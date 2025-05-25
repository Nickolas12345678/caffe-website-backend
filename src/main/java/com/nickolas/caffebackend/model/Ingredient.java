package com.nickolas.caffebackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "ingredient")
@Data
public class Ingredient {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

    private String quantity;
    private String name;
//    private int availableQuantity;
    private String unit;

    @ManyToOne
    @JoinColumn(name = "ingredient_stock_id")
    private IngredientStock ingredientStock;

    @ManyToOne
    @JoinColumn(name = "dish_id")
    @JsonBackReference
    private Dish dish;
}
