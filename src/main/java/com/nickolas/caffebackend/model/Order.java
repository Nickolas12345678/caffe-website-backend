package com.nickolas.caffebackend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.nickolas.caffebackend.domain.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Представляє замовлення користувача, що складається з кількох товарів (страв).
 * Містить інформацію про користувача, статус, номер телефону, тип та адресу доставки.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {
    /** Унікальний ідентифікатор замовлення. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Користувач, який зробив замовлення. */
    @ManyToOne
    private User user;

    /** Поточний статус замовлення (очікується, готується, завершено, скасовано). */
    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;

    /** Список товарів у замовленні. */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<OrderItem> items = new ArrayList<>();

    /** Контактний номер телефону для доставки. */
    private String phoneNumber;

    /** Адреса доставки (якщо застосовується). */
    private String deliveryAddress;

    /** Тип доставки. */
    private String deliveryType;
}
