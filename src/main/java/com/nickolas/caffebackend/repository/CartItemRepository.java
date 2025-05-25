package com.nickolas.caffebackend.repository;

import com.nickolas.caffebackend.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
