package com.nickolas.caffebackend.request;

import lombok.Data;

/**
 * DTO для створення замовлення.
 */
@Data
public class CreateOrderRequest {
    private String phoneNumber;
//    private String deliveryAddress;
    private String deliveryType; // "pickup" або "delivery"

    private String city;
    private String street;
    private String building;
    private String apartment;

    private String pickupPoint;
}
