package com.nickolas.caffebackend.service;

import com.nickolas.caffebackend.domain.OrderStatus;
import com.nickolas.caffebackend.model.*;
import com.nickolas.caffebackend.repository.OrderRepository;
import com.nickolas.caffebackend.repository.UserRepository;
import com.nickolas.caffebackend.request.CreateOrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервіс для обробки замовлень.
 * Відповідає за створення, оновлення статусу, скасування та отримання замовлень.
 */
@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Створює нове замовлення на основі вмісту кошика користувача.
     *
     * @param email   електронна пошта користувача
     * @param request дані для створення замовлення (телефон, тип доставки, адреса або пункт самовивозу)
     * @return створене замовлення
     * @throws RuntimeException якщо користувач не знайдений, кошик порожній або дані доставки некоректні
     */
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
        order.setDeliveryType(request.getDeliveryType());

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
        else if ("pickup".equalsIgnoreCase(request.getDeliveryType())) {
            if (request.getPickupPoint() == null || request.getPickupPoint().isBlank()) {
                throw new RuntimeException("Pickup point is required for self-pickup");
            }
            order.setDeliveryAddress("Самовивіз: " + request.getPickupPoint());
        } else {
            throw new RuntimeException("Invalid delivery type");
        }

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

    /**
     * Повертає список замовлень користувача.
     *
     * @param email електронна пошта користувача
     * @return список замовлень
     * @throws RuntimeException якщо користувач не знайдений
     */
    public List<Order> getOrdersForUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return orderRepository.findByUser(user);
    }

    /**
     * Оновлює статус замовлення. Дозволяється лише послідовна зміна (наприклад, з NEW → PROCESSING).
     *
     * @param orderId   ідентифікатор замовлення
     * @param newStatus новий статус
     * @return оновлене замовлення
     * @throws RuntimeException якщо замовлення не знайдене або статус змінено некоректно
     */
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

    /**
     * Скасовує замовлення, якщо воно в статусі PENDING і належить вказаному користувачу.
     *
     * @param orderId ідентифікатор замовлення
     * @param email   електронна пошта користувача
     * @return оновлене замовлення зі статусом CANCELLED
     * @throws RuntimeException якщо користувач не авторизований або статус не дозволяє скасування
     */
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

    /**
     * Повертає всі замовлення в системі (для адміністратора).
     *
     * @return список усіх замовлень
     */
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
