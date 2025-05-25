package com.nickolas.caffebackend.controller;

import com.nickolas.caffebackend.model.Ingredient;
import com.nickolas.caffebackend.service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@RestController
//@RequestMapping("/api/ingredients")
//public class IngredientController {
//    private final IngredientService ingredientService;
//
//    @Autowired
//    public IngredientController(IngredientService ingredientService) {
//        this.ingredientService = ingredientService;
//    }
//
//    @GetMapping
//    public List<Ingredient> getAllIngredients() {
//        return ingredientService.getAllIngredients();
//    }
//
//    @PostMapping
//    public ResponseEntity<Ingredient> createIngredient(@RequestBody Ingredient ingredient) {
//        return ResponseEntity.status(HttpStatus.CREATED).body(ingredientService.createIngredient(ingredient));
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Ingredient> updateIngredient(@PathVariable Long id, @RequestBody Ingredient ingredient) {
//        return ResponseEntity.ok(ingredientService.updateIngredient(id, ingredient));
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteIngredient(@PathVariable Long id) {
//        ingredientService.deleteIngredient(id);
//        return ResponseEntity.noContent().build();
//    }
//}
