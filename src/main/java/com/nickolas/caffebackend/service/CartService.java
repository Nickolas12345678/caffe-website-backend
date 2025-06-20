package com.nickolas.caffebackend.service;

import com.nickolas.caffebackend.model.Cart;
import com.nickolas.caffebackend.model.CartItem;
import com.nickolas.caffebackend.model.Dish;
import com.nickolas.caffebackend.model.User;
import com.nickolas.caffebackend.repository.CartRepository;
import com.nickolas.caffebackend.repository.DishRepository;
import com.nickolas.caffebackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Сервіс для керування кошиком користувача.
 * Дозволяє додавати, оновлювати, видаляти страви з кошика,
 * а також очищати весь кошик.
 */
@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Отримує кошик користувача за його email.
     * Якщо кошик не існує, створюється новий.
     *
     * @param email електронна пошта користувача
     * @return об'єкт {@link Cart}
     */
    public Cart getCartByUserEmail(String email) {
        return cartRepository.findByUserEmail(email)
                .orElseGet(() -> createCartForUser(email));
    }


    /**
     * Створює новий кошик для користувача.
     *
     * @param email електронна пошта користувача
     * @return створений об'єкт {@link Cart}
     * @throws RuntimeException якщо користувача не знайдено
     */
    private Cart createCartForUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Cart cart = new Cart();
        cart.setUser(user);
        return cartRepository.save(cart);
    }

    /**
     * Додає страву до кошика користувача або збільшує кількість,
     * якщо страва вже є в кошику.
     *
     * @param email електронна пошта користувача
     * @param dishId ідентифікатор страви
     * @param quantity кількість
     * @return оновлений об'єкт {@link Cart}
     * @throws RuntimeException якщо страву не знайдено
     */
    // Додавання страви до кошика
    public Cart addDishToCart(String email, Long dishId, int quantity) {
        Cart cart = getCartByUserEmail(email);
        Dish dish = dishRepository.findById(dishId)
                .orElseThrow(() -> new RuntimeException("Dish not found"));

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getDish().getId().equals(dishId))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setDish(dish);
            newItem.setQuantity(quantity);
            cart.getItems().add(newItem);
        }

        return cartRepository.save(cart);
    }

    /**
     * Оновлює кількість певної страви в кошику користувача.
     *
     * @param email електронна пошта користувача
     * @param dishId ідентифікатор страви
     * @param quantity нова кількість
     * @return оновлений об'єкт {@link Cart}
     */
    public Cart updateDishQuantity(String email, Long dishId, int quantity) {
        Cart cart = getCartByUserEmail(email);
        cart.getItems().forEach(item -> {
            if (item.getDish().getId().equals(dishId)) {
                item.setQuantity(quantity);
            }
        });
        return cartRepository.save(cart);
    }

    /**
     * Видаляє певну страву з кошика користувача.
     *
     * @param email електронна пошта користувача
     * @param dishId ідентифікатор страви
     * @return оновлений об'єкт {@link Cart}
     */
    public Cart removeDishFromCart(String email, Long dishId) {
        Cart cart = getCartByUserEmail(email);
        cart.getItems().removeIf(item -> item.getDish().getId().equals(dishId));
        return cartRepository.save(cart);
    }

    /**
     * Очищує весь кошик користувача.
     *
     * @param email електронна пошта користувача
     */
    public void clearCart(String email) {
        Cart cart = getCartByUserEmail(email);
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}
