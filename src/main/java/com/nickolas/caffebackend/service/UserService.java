package com.nickolas.caffebackend.service;

import com.nickolas.caffebackend.config.JwtProvider;
import com.nickolas.caffebackend.domain.Role;
import com.nickolas.caffebackend.exception.UserException;
import com.nickolas.caffebackend.model.User;
import com.nickolas.caffebackend.repository.UserRepository;
import com.nickolas.caffebackend.request.SignupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервіс для обробки логіки, пов'язаної з користувачами.
 * Включає отримання профілю, зміну ролі, авторизацію, видалення та інше.
 */
@Service
public class UserService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    /**
     * Конструктор UserService.
     *
     * @param userRepository репозиторій користувачів
     * @param jwtProvider    провайдер для розбору JWT токенів
     */
    public UserService(UserRepository userRepository, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
    }

    /**
     * Повертає користувача, профіль якого витягується з JWT токена.
     *
     * @param jwt JWT токен
     * @return користувач, відповідний email у токені
     * @throws UserException якщо користувача з таким email не існує
     */
    public User findUserProfileByJwt(String jwt) throws UserException {
        String email = jwtProvider.getEmailFromToken(jwt);


        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException("Користувача не існує з email: " + email));

        return user;
    }

    /**
     * Повертає користувача за ім'ям користувача (email).
     *
     * @param username email користувача
     * @return об'єкт користувача
     * @throws UserException якщо користувача не знайдено
     */
    public User getByUsername(String username) {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UserException("Користувача не знайдено"));

    }

    /**
     * Змінює роль користувача.
     *
     * @param userId  ідентифікатор користувача
     * @param newRole нова роль
     * @return оновлений користувач
     * @throws UserException якщо користувача не знайдено
     */
    public User changeUserRole(Long userId, Role newRole) throws UserException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException("Користувача не знайдено"));

        user.setRole(newRole);
        return userRepository.save(user);
    }

    /**
     * Перевіряє, чи має користувач задану роль.
     *
     * @param email email користувача
     * @param role  роль для перевірки
     * @return true, якщо роль збігається
     * @throws UserException якщо користувача не знайдено
     */
    public boolean hasRole(String email, Role role) throws UserException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException("Користувача не існує з email: " + email));
        return user.getRole().equals(role);
    }

    /**
     * Повертає список усіх користувачів.
     *
     * @return список користувачів
     */
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Видаляє користувача за ідентифікатором.
     *
     * @param userId ідентифікатор користувача
     * @throws UserException якщо користувача не знайдено
     */
    public void deleteUserById(Long userId) throws UserException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException("User not found"));

        userRepository.delete(user);
    }

    /**
     * Повертає реалізацію {@link UserDetailsService} для використання в Spring Security.
     *
     * @return UserDetailsService, який знаходить користувача за email
     */
    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }
}
