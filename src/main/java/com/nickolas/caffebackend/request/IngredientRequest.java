package com.nickolas.caffebackend.request;

import lombok.Data;

@Data
public class IngredientRequest {
    private String name;
    private String quantity;
}
