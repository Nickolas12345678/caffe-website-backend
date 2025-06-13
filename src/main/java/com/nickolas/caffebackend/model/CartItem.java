package com.nickolas.caffebackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

/**
 * Окремий елемент кошика, що представляє одну позицію (страву) та її кількість.
 */
@Entity
@Table(name = "cart_items")
@Data
public class CartItem {

    /**
     * Унікальний ідентифікатор елемента кошика.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Кошик, до якого належить цей елемент.
     */
    @ManyToOne
    @JsonBackReference
    private Cart cart;

    /**
     * Страва, яку додано до кошика.
     */
    @ManyToOne
    private Dish dish;

    /**
     * Кількість обраної страви.
     */
    private int quantity;
}
