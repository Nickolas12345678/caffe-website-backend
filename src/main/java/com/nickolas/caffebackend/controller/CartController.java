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

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private JwtProvider jwtProvider;

    @GetMapping
    public ResponseEntity<Cart> getCart(HttpServletRequest request) {
        String email = jwtProvider.getEmailFromToken(request.getHeader("Authorization"));
        if (email == null) {
            return ResponseEntity.status(401).body(null); // Unauthorized
        }

        Cart cart = cartService.getCartByUserEmail(email);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/add")
    public ResponseEntity<Cart> addToCart(HttpServletRequest request, @RequestBody CartItemRequest cartItemRequest) {
        String email = jwtProvider.getEmailFromToken(request.getHeader("Authorization"));
        if (email == null) {
            return ResponseEntity.status(401).body(null);
        }

        Cart cart = cartService.addDishToCart(email, cartItemRequest.getDishId(), cartItemRequest.getQuantity());
        return ResponseEntity.ok(cart);
    }

    @PutMapping("/update")
    public ResponseEntity<Cart> updateQuantity(HttpServletRequest request, @RequestBody CartItemRequest cartItemRequest) {
        String email = jwtProvider.getEmailFromToken(request.getHeader("Authorization"));
        if (email == null) {
            return ResponseEntity.status(401).body(null); // Unauthorized
        }

        Cart cart = cartService.updateDishQuantity(email, cartItemRequest.getDishId(), cartItemRequest.getQuantity());
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Cart> removeDish(HttpServletRequest request, @RequestBody CartItemRequest cartItemRequest) {
        String email = jwtProvider.getEmailFromToken(request.getHeader("Authorization"));
        if (email == null) {
            return ResponseEntity.status(401).body(null); // Unauthorized
        }
        Cart cart = cartService.removeDishFromCart(email, cartItemRequest.getDishId());
        return ResponseEntity.ok(cart);
    }

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
