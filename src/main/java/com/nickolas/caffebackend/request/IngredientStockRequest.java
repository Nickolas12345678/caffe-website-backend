package com.nickolas.caffebackend.request;

import lombok.Data;

/**
 * DTO для заповнення/оновлення складу інгредієнтів.
 */
@Data
public class IngredientStockRequest {
    private String name;
    private String availableQuantity;
    private String unit;
}
