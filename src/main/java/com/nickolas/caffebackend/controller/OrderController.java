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

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private JwtProvider jwtProvider;


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

    @GetMapping("/my")
    public ResponseEntity<List<Order>> getMyOrders(HttpServletRequest request) {
        String email = jwtProvider.getEmailFromToken(request.getHeader("Authorization"));
        if (email == null) return ResponseEntity.status(401).build();

        return ResponseEntity.ok(orderService.getOrdersForUser(email));
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<Order> updateStatus(@PathVariable("orderId") Long orderId, @RequestParam("status") OrderStatus status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, status));
    }

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


    @GetMapping("/all")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }
}
