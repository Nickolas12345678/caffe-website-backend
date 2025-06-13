package com.nickolas.caffebackend.repository;

import com.nickolas.caffebackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Репозиторій для роботи з користувачами {@link com.nickolas.caffebackend.model.User}.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Пошук користувача за email.
     */
    Optional<User> findByEmail(String email);

    /**
     * Перевірка, чи існує користувач з таким email.
     */
    boolean existsByEmail(String email);
}
