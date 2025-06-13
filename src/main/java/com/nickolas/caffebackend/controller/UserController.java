package com.nickolas.caffebackend.controller;

import com.nickolas.caffebackend.config.JwtProvider;
import com.nickolas.caffebackend.exception.UserException;
import com.nickolas.caffebackend.model.User;
import com.nickolas.caffebackend.request.RoleChangeRequest;
import com.nickolas.caffebackend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * REST-контролер для роботи з користувачами.
 * Надає функціонал для отримання профілю користувача, перегляду всіх користувачів (адмін),
 * зміни ролі та видалення користувача.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final JwtProvider jwtProvider;

    /**
     * Конструктор для впровадження залежностей.
     *
     * @param userService сервіс користувачів
     * @param jwtProvider провайдер для обробки JWT
     */
    public UserController(UserService userService, JwtProvider jwtProvider) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
    }

    /**
     * Отримати профіль поточного користувача на основі JWT.
     *
     * @param jwt токен авторизації у форматі Bearer
     * @return профіль користувача
     * @throws UserException у випадку помилок доступу або некоректного токена
     */
    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfileHandler(
            @RequestHeader("Authorization") String jwt) throws UserException {

        System.out.println("/api/users/profile");
        User user = userService.findUserProfileByJwt(jwt);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Отримати список усіх користувачів (доступно лише адміну).
     *
     * @param jwt токен авторизації
     * @return список користувачів
     * @throws UserException у випадку помилки доступу
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers(@RequestHeader("Authorization") String jwt) throws UserException {
        List<User> users = userService.findAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /**
     * Змінити роль користувача (доступно лише адміну).
     *
     * @param jwt                токен авторизації
     * @param userId             ідентифікатор користувача
     * @param roleChangeRequest  об'єкт з новою роллю
     * @return оновлений користувач
     * @throws UserException у разі помилки або відсутності користувача
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/role/{userId}")
    public ResponseEntity<User> changeUserRole(
            @RequestHeader("Authorization") String jwt,
            @PathVariable("userId") Long userId,
            @RequestBody RoleChangeRequest roleChangeRequest) throws UserException {


        User updatedUser = userService.changeUserRole(userId, roleChangeRequest.getRole());


        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    /**
     * Видалити користувача за його ID (доступно лише адміну).
     *
     * @param jwt    токен авторизації
     * @param userId ідентифікатор користувача
     * @return повідомлення про успішне видалення
     * @throws UserException якщо користувач не знайдений або є помилка доступу
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(
            @RequestHeader("Authorization") String jwt,
            @PathVariable("userId") Long userId) throws UserException {

        userService.deleteUserById(userId);
        return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
    }
}
