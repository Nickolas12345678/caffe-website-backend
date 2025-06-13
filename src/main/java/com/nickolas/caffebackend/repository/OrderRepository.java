package com.nickolas.caffebackend.repository;

import com.nickolas.caffebackend.model.Order;
import com.nickolas.caffebackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Репозиторій для роботи із замовленнями {@link com.nickolas.caffebackend.model.Order}.
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Знаходить усі замовлення, що належать конкретному користувачу.
     */
    List<Order> findByUser(User user);
}
