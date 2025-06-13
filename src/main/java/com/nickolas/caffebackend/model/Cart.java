package com.nickolas.caffebackend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Модель кошика користувача.
 * Містить список товарів (страв), які користувач додав у кошик.
 */
@Entity
@Table(name = "cart")
@Data
public class Cart {
    /**
     * Унікальний ідентифікатор кошика.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Користувач, якому належить кошик.
     */
    @OneToOne
    private User user;

    /**
     * Список елементів кошика.
     */
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<CartItem> items = new ArrayList<>();
}
