package com.nickolas.caffebackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Модель страви, яка продається в закладі.
 * Містить інформацію про назву, опис, ціну, вагу та інгредієнти.
 */
@Entity
@Table(name = "dishes")
@Data
public class Dish {

/** Унікальний ідентифікатор страви. */
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

    /** Назва страви. */
    private String name;

    /** Детальний опис страви. */
    @Column(length = 2000)
    private String description;

    /** Ціна страви. */
    private double price;

    /** URL зображення страви. */
    private String imageUrl;

    /** Вага страви. */
    private String weight;

    /** Час приготування. */
    private String preparationTime;

    /** Категорія, до якої належить страва. */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    /** Список інгредієнтів, з яких складається страва. */
    @OneToMany(mappedBy = "dish", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Ingredient> ingredients = new ArrayList<>();
}
