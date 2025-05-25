package com.nickolas.caffebackend.repository;

import com.nickolas.caffebackend.model.Order;
import com.nickolas.caffebackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}
