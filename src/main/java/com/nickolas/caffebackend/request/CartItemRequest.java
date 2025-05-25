package com.nickolas.caffebackend.request;

import lombok.Data;

@Data
public class CartItemRequest {
    private Long dishId;
    private int quantity;
}
