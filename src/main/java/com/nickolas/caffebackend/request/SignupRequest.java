package com.nickolas.caffebackend.request;

import com.nickolas.caffebackend.domain.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO для реєстрації нового користувача.
 */
@Data
public class SignupRequest {
    @NotBlank(message = "Ім'я користувача не може бути порожнім")
    @Size(min = 2, message = "Ім'я користувача повинно містити хоча б 2 символи")
    private String username;

    @NotBlank(message = "Email не може бути порожнім")
    @Email(message = "Невірний формат email")
    private String email;

    @NotBlank(message = "Пароль не може бути порожнім")
    @Size(min = 6, message = "Пароль повинен містити хоча б 6 символів")
    private String password;
}
