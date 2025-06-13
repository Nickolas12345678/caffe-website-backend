package com.nickolas.caffebackend.repository;

import com.nickolas.caffebackend.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторій для доступу до об'єктів {@link com.nickolas.caffebackend.model.CartItem}.
 */
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
