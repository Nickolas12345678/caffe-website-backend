package com.nickolas.caffebackend.service;

import com.nickolas.caffebackend.domain.OrderStatus;
import com.nickolas.caffebackend.model.*;
import com.nickolas.caffebackend.repository.OrderRepository;
import com.nickolas.caffebackend.repository.UserRepository;
import com.nickolas.caffebackend.request.CreateOrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserRepository userRepository;

    public Order createOrder(String email, CreateOrderRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartService.getCartByUserEmail(email);

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        if (request.getPhoneNumber() == null || request.getPhoneNumber().isBlank()) {
            throw new RuntimeException("Phone number is required");
        }

        Order order = new Order();
        order.setUser(user);
        order.setPhoneNumber(request.getPhoneNumber());
        order.setDeliveryType(request.getDeliveryType()); // Визначаємо тип доставки

        // Якщо вибрана доставка, зберігаємо адресу доставки
        if ("delivery".equalsIgnoreCase(request.getDeliveryType())) {
            if (request.getCity() == null || request.getStreet() == null || request.getBuilding() == null) {
                throw new RuntimeException("City, street and building are required for delivery");
            }

            StringBuilder address = new StringBuilder();
            address.append("м. ").append(request.getCity())
                    .append(", вул. ").append(request.getStreet())
                    .append(", буд. ").append(request.getBuilding());

            if (request.getApartment() != null && !request.getApartment().isBlank()) {
                address.append(", кв. ").append(request.getApartment());
            }

            order.setDeliveryAddress(address.toString());
        }
        // Якщо самовивіз, зберігаємо тільки точку самовивозу
        else if ("pickup".equalsIgnoreCase(request.getDeliveryType())) {
            if (request.getPickupPoint() == null || request.getPickupPoint().isBlank()) {
                throw new RuntimeException("Pickup point is required for self-pickup");
            }
            order.setDeliveryAddress("Самовивіз: " + request.getPickupPoint());
        } else {
            throw new RuntimeException("Invalid delivery type");
        }

        // Додаємо товари до замовлення
        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setDish(cartItem.getDish());
            orderItem.setQuantity(cartItem.getQuantity());
            order.getItems().add(orderItem);
        }

        cartService.clearCart(email);
        return orderRepository.save(order);
    }



    public List<Order> getOrdersForUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return orderRepository.findByUser(user);
    }

    public Order updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        OrderStatus currentStatus = order.getStatus();

        if (currentStatus == OrderStatus.COMPLETED || currentStatus == OrderStatus.CANCELLED) {
            throw new RuntimeException("Cannot change status after it is " + currentStatus);
        }

        OrderStatus[] statusFlow = OrderStatus.values();
        int currentIndex = currentStatus.ordinal();
        int newIndex = newStatus.ordinal();

        if (newIndex == currentIndex + 1) {
            order.setStatus(newStatus);
            return orderRepository.save(order);
        } else {
            throw new RuntimeException("Invalid status transition from " + currentStatus + " to " + newStatus);
        }
    }

    public Order cancelOrder(Long orderId, String email) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().getEmail().equals(email)) {
            throw new RuntimeException("You are not authorized to cancel this order");
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Order cannot be cancelled in its current status");
        }

        order.setStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }



    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
