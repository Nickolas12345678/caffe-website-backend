package com.nickolas.caffebackend.controller;


import com.nickolas.caffebackend.model.Dish;
import com.nickolas.caffebackend.request.DishCreateRequest;
import com.nickolas.caffebackend.request.DishUpdateRequest;
import com.nickolas.caffebackend.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/dishes")
public class DishController {
    private final DishService dishService;

    @Autowired
    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @GetMapping
    public ResponseEntity<Page<Dish>> getAllDishes(@RequestParam(name = "page", defaultValue = "0") int page,
                                                   @RequestParam(name = "size", defaultValue = "10") int size) {
        Page<Dish> dishes = dishService.getAllDishes(PageRequest.of(page, size));
        return ResponseEntity.ok(dishes);
    }


//@GetMapping("/category/{categoryId}")
//public ResponseEntity<Page<Dish>> getDishesByCategory(
//        @PathVariable("categoryId") Long categoryId,
//        @RequestParam(name = "page", defaultValue = "0") int page,
//        @RequestParam(name = "size", defaultValue = "10") int size,
//        @RequestParam(name = "minPrice", required = false) Double minPrice,
//        @RequestParam(name = "maxPrice", required = false) Double maxPrice,
//        @RequestParam(name = "sort", required = false, defaultValue = "asc") String sortOrder) {
//
//    PageRequest pageRequest = PageRequest.of(page, size);
//    Page<Dish> dishes = dishService.getDishesByCategory(categoryId, pageRequest, minPrice, maxPrice, sortOrder);
//
//    return ResponseEntity.ok(dishes);
//}
@GetMapping("/category/{categoryId}")
public ResponseEntity<Page<Dish>> getDishesByCategory(
        @PathVariable("categoryId") Long categoryId,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size,
        @RequestParam(name = "minPrice", required = false) Double minPrice,
        @RequestParam(name = "maxPrice", required = false) Double maxPrice,
        @RequestParam(name = "sort", required = false, defaultValue = "") String sortOrder,  // Параметр для сортування
        @RequestParam(name = "name", required = false) String name) {

    PageRequest pageRequest = PageRequest.of(page, size);

    // Якщо сортування не задано, не застосовуємо сортування
    if (sortOrder.isEmpty()) {
        sortOrder = null;
    }

    Page<Dish> dishes;

    // Якщо є фільтр по ціні, застосовуємо його
    if (minPrice != null || maxPrice != null) {
        dishes = dishService.getDishesByCategoryWithPriceFilter(categoryId, pageRequest, minPrice, maxPrice, sortOrder, name);
    } else {
        // Якщо немає фільтру по ціні, просто повертаємо страви без фільтрації за ціною
        dishes = dishService.getDishesByCategory(categoryId, pageRequest, sortOrder, name);
    }

    return ResponseEntity.ok(dishes);
}




    @GetMapping("/{id}")
    public ResponseEntity<Dish> getDishById(@PathVariable("id") Long id) {
        Optional<Dish> dish = dishService.getDishById(id);
        return dish.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Dish> createDish(@RequestBody DishCreateRequest request) {
        Dish createdDish = dishService.createDish(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDish);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Dish> updateDish(@PathVariable("id") Long id, @RequestBody DishUpdateRequest request) {
        try {
            Dish updatedDish = dishService.updateDish(id, request);
            return ResponseEntity.ok(updatedDish);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDish(@PathVariable("id") Long id) {
        try {
            dishService.deleteDish(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
