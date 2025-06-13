package com.nickolas.caffebackend.repository;

import com.nickolas.caffebackend.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Репозиторій для об'єктів {@link com.nickolas.caffebackend.model.Cart}.
 * Забезпечує доступ до кошика користувача за email.
 */
public interface CartRepository extends JpaRepository<Cart, Long> {

    /**
     * Знаходить кошик користувача за його email.
     *
     * @param email електронна адреса користувача
     * @return необов'язковий {@link Cart}, якщо знайдено
     */
    Optional<Cart> findByUserEmail(String email);
}
