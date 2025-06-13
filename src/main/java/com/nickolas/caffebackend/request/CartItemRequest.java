package com.nickolas.caffebackend.request;

import lombok.Data;

/**
 * DTO для додавання елемента до кошика.
 */
@Data
public class CartItemRequest {
    private Long dishId;
    private int quantity;
}
