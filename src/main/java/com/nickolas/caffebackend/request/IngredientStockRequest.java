package com.nickolas.caffebackend.request;

import lombok.Data;

@Data
public class IngredientStockRequest {
    private String name;
    private String availableQuantity;
    private String unit;
}
