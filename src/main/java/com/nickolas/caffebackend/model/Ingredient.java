package com.nickolas.caffebackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Ingredient {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

    private String quantity;
    private String name;

    @ManyToOne
    @JoinColumn(name = "dish_id")
    @JsonBackReference
    private Dish dish;
}
