package com.nickolas.caffebackend.request;

import lombok.Data;

import java.util.List;

/**
 * DTO для створення нової страви.
 */
@Data
public class DishCreateRequest {
    private String name;
    private String description;
    private double price;
    private String imageUrl;
    private String weight;
    private String preparationTime;
    private Long categoryId;
    private List<IngredientRequest> ingredients;
}
