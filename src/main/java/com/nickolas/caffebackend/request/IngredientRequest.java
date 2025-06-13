package com.nickolas.caffebackend.request;

import lombok.Data;

/**
 * DTO для опису інгредієнта страви.
 */
@Data
public class IngredientRequest {
    private String name;
    private String quantity;
    private String unit;
//    private int availableQuantity;
}
