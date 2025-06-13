package com.nickolas.caffebackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Представляє одну позицію замовлення, що містить конкретну страву та її кількість.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_item")
public class OrderItem {

    /** Унікальний ідентифікатор товару у замовленні. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Замовлення, до якого належить цей товар. */
    @ManyToOne
    @JsonBackReference
    private Order order;

    /** Страва, яка була замовлена. */
    @ManyToOne
    private Dish dish;

    /** Кількість одиниць цієї страви в замовленні. */
    private int quantity;
}
