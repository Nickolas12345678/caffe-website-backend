package com.nickolas.caffebackend.controller;

import com.nickolas.caffebackend.model.IngredientStock;
import com.nickolas.caffebackend.request.IngredientStockRequest;
import com.nickolas.caffebackend.service.IngredientStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingredient-stocks")
public class IngredientStockController {
    private final IngredientStockService ingredientStockService;

    @Autowired
    public IngredientStockController(IngredientStockService ingredientStockService) {
        this.ingredientStockService = ingredientStockService;
    }

    @GetMapping
    public List<IngredientStock> getAllIngredientStocks() {
        return ingredientStockService.getAllIngredientStocks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngredientStock> getIngredientStockById(@PathVariable("id") Long id) {
        return ingredientStockService.getIngredientStockById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    @PostMapping
    public ResponseEntity<IngredientStock> createIngredientStock(@RequestBody IngredientStockRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ingredientStockService.createIngredientStock(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IngredientStock> updateIngredientStock(@PathVariable("id") Long id,
                                                                 @RequestBody IngredientStockRequest request) {
        try {
            return ResponseEntity.ok(ingredientStockService.updateIngredientStock(id, request));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIngredientStock(@PathVariable("id") Long id) {
        try {
            ingredientStockService.deleteIngredientStock(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
