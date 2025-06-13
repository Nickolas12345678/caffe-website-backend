package com.nickolas.caffebackend.controller;

import com.nickolas.caffebackend.config.JwtProvider;
import com.nickolas.caffebackend.domain.OrderStatus;
import com.nickolas.caffebackend.model.Order;
import com.nickolas.caffebackend.request.CreateOrderRequest;
import com.nickolas.caffebackend.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST-контролер для керування замовленнями.
 * Забезпечує створення, перегляд, оновлення статусу та скасування замовлень.
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private JwtProvider jwtProvider;

    /**
     * Створити нове замовлення для автентифікованого користувача.
     *
     * @param request     тіло запиту з даними замовлення
     * @param httpRequest HTTP-запит для отримання токена авторизації
     * @return створене замовлення або статус помилки
     */
@PostMapping("/create")
public ResponseEntity<Order> createOrder(@RequestBody CreateOrderRequest request, HttpServletRequest httpRequest) {
    String email = jwtProvider.getEmailFromToken(httpRequest.getHeader("Authorization"));
    if (email == null) return ResponseEntity.status(401).build();

    if (request.getPhoneNumber() == null || request.getDeliveryType() == null) {
        return ResponseEntity.badRequest().body(null);
    }

    Order order = orderService.createOrder(email, request);
    return ResponseEntity.ok(order);
}

    /**
     * Отримати список замовлень поточного користувача.
     *
     * @param request HTTP-запит для витягування токена
     * @return список замовлень користувача або статус 401, якщо користувач не авторизований
     */
    @GetMapping("/my")
    public ResponseEntity<List<Order>> getMyOrders(HttpServletRequest request) {
        String email = jwtProvider.getEmailFromToken(request.getHeader("Authorization"));
        if (email == null) return ResponseEntity.status(401).build();

        return ResponseEntity.ok(orderService.getOrdersForUser(email));
    }

    /**
     * Оновити статус замовлення.
     *
     * @param orderId ідентифікатор замовлення
     * @param status  новий статус замовлення
     * @return оновлене замовлення
     */
    @PutMapping("/{orderId}/status")
    public ResponseEntity<Order> updateStatus(@PathVariable("orderId") Long orderId, @RequestParam("status") OrderStatus status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, status));
    }

    /**
     * Скасувати замовлення користувачем.
     *
     * @param orderId ідентифікатор замовлення
     * @param request HTTP-запит для витягування токена
     * @return оновлене (скасоване) замовлення або статус помилки
     */
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Order> cancelOrder(@PathVariable("orderId") Long orderId, HttpServletRequest request) {
        String email = jwtProvider.getEmailFromToken(request.getHeader("Authorization"));
        if (email == null) return ResponseEntity.status(401).build();

        try {
            Order cancelledOrder = orderService.cancelOrder(orderId, email);
            return ResponseEntity.ok(cancelledOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Отримати всі замовлення (адміністративний доступ).
     *
     * @return список усіх замовлень
     */
    @GetMapping("/all")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }
}
