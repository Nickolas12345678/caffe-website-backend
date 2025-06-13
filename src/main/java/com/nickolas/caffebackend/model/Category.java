package com.nickolas.caffebackend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Категорія страв.
 * Кожна страва належить до певної категорії.
 */
@Entity
@Table(name = "categories")
@Data
public class Category {
    /** Унікальний ідентифікатор категорії. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Назва категорії. */
    private String name;

    /** Опис категорії. */
    private String description;

    /** URL зображення для категорії. */
    private String imageUrl;
}
