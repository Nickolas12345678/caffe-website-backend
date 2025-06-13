package com.nickolas.caffebackend.request;

import lombok.Data;

/**
 * DTO для авторизації користувача.
 */
@Data
public class SigninRequest {
    private String email;
    private String password;
}
