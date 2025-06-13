package com.nickolas.caffebackend.request;

import com.nickolas.caffebackend.model.Ingredient;
import lombok.Data;
import lombok.Getter;

import java.util.List;

/**
 * DTO для оновлення страви.
 */
@Data
@Getter
public class DishUpdateRequest {
    private String name;
    private String description;
    private double price;
    private String imageUrl;
    private String weight;
    private String preparationTime;
    private Long categoryId;
    private List<IngredientRequest> ingredients;
}
