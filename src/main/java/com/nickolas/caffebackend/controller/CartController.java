package com.nickolas.caffebackend.controller;

import com.nickolas.caffebackend.config.JwtProvider;
import com.nickolas.caffebackend.model.Cart;
import com.nickolas.caffebackend.model.CartItem;
import com.nickolas.caffebackend.request.CartItemRequest;
import com.nickolas.caffebackend.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Контролер для управління кошиком користувача.
 * Забезпечує функціональність перегляду, додавання, оновлення та видалення страв з кошика.
 */
@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private JwtProvider jwtProvider;

    /**
     * Отримати поточний кошик користувача.
     *
     * @param request HTTP-запит, який містить JWT-токен в заголовку Authorization
     * @return Об'єкт {@link Cart} у відповіді або статус 401, якщо користувач неавторизований
     */
    @GetMapping
    public ResponseEntity<Cart> getCart(HttpServletRequest request) {
        String email = jwtProvider.getEmailFromToken(request.getHeader("Authorization"));
        if (email == null) {
            return ResponseEntity.status(401).body(null); // Unauthorized
        }

        Cart cart = cartService.getCartByUserEmail(email);
        return ResponseEntity.ok(cart);
    }

    /**
     * Додати страву до кошика користувача.
     *
     * @param request HTTP-запит з JWT-токеном
     * @param cartItemRequest Об'єкт, який містить ідентифікатор страви та кількість
     * @return Оновлений кошик або статус 401 у разі відсутності авторизації
     */
    @PostMapping("/add")
    public ResponseEntity<Cart> addToCart(HttpServletRequest request, @RequestBody CartItemRequest cartItemRequest) {
        String email = jwtProvider.getEmailFromToken(request.getHeader("Authorization"));
        if (email == null) {
            return ResponseEntity.status(401).body(null);
        }

        Cart cart = cartService.addDishToCart(email, cartItemRequest.getDishId(), cartItemRequest.getQuantity());
        return ResponseEntity.ok(cart);
    }

    /**
     * Оновити кількість певної страви в кошику користувача.
     *
     * @param request HTTP-запит з JWT-токеном
     * @param cartItemRequest Об'єкт із даними про страву та нову кількість
     * @return Оновлений кошик або статус 401, якщо користувач неавторизований
     */
    @PutMapping("/update")
    public ResponseEntity<Cart> updateQuantity(HttpServletRequest request, @RequestBody CartItemRequest cartItemRequest) {
        String email = jwtProvider.getEmailFromToken(request.getHeader("Authorization"));
        if (email == null) {
            return ResponseEntity.status(401).body(null); // Unauthorized
        }

        Cart cart = cartService.updateDishQuantity(email, cartItemRequest.getDishId(), cartItemRequest.getQuantity());
        return ResponseEntity.ok(cart);
    }

    /**
     * Видалити страву з кошика користувача.
     *
     * @param request HTTP-запит з JWT-токеном
     * @param cartItemRequest Об'єкт із ідентифікатором страви, яку потрібно видалити
     * @return Оновлений кошик або статус 401, якщо користувач неавторизований
     */
    @DeleteMapping("/remove")
    public ResponseEntity<Cart> removeDish(HttpServletRequest request, @RequestBody CartItemRequest cartItemRequest) {
        String email = jwtProvider.getEmailFromToken(request.getHeader("Authorization"));
        if (email == null) {
            return ResponseEntity.status(401).body(null); // Unauthorized
        }
        Cart cart = cartService.removeDishFromCart(email, cartItemRequest.getDishId());
        return ResponseEntity.ok(cart);
    }

    /**
     * Очистити весь кошик користувача.
     *
     * @param request HTTP-запит з JWT-токеном
     * @return Статус 204 (No Content) у разі успішного очищення або статус 401 у разі відсутності авторизації
     */
    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(HttpServletRequest request) {
        String email = jwtProvider.getEmailFromToken(request.getHeader("Authorization"));
        if (email == null) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }

        cartService.clearCart(email);
        return ResponseEntity.noContent().build();
    }
}
