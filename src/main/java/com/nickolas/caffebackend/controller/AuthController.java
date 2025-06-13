package com.nickolas.caffebackend.controller;

import com.nickolas.caffebackend.model.User;
import com.nickolas.caffebackend.request.SigninRequest;
import com.nickolas.caffebackend.request.SignupRequest;
import com.nickolas.caffebackend.response.AuthResponse;
import com.nickolas.caffebackend.service.AuthService;
import com.nickolas.caffebackend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Контролер для обробки запитів, пов’язаних з автентифікацією користувачів.
 * Містить ендпоінти для реєстрації та входу в систему.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    /**
     * Обробляє запит на реєстрацію нового користувача.
     *
     * @param signupRequest Об'єкт, що містить дані для реєстрації (логін, пароль, тощо)
     * @param bindingResult Результати валідації вхідного запиту
     * @return {@link ResponseEntity} з JWT-токеном та роллю користувача у разі успішної реєстрації,
     * або повідомлення про помилки валідації/реєстрації
     */
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

            return ResponseEntity.badRequest().body(errors);
        }

        try {
            User user = authService.registerUser(signupRequest);
            String token = authService.generateToken(user);

            AuthResponse authResponse = new AuthResponse();
            authResponse.setJwt(token);
            authResponse.setMessage("Реєстрація успішна");
            authResponse.setRole(user.getRole().name());

            return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }


    /**
     * Обробляє запит на вхід до системи.
     *
     * @param signinRequest Об'єкт, що містить дані для входу (логін, пароль)
     * @return {@link ResponseEntity} з JWT-токеном у разі успішної автентифікації,
     * або повідомленням про помилку у разі невдалої спроби входу
     */
    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signin(@RequestBody SigninRequest signinRequest) {

        try{
            AuthResponse authResponse = authService.authenticateUser(signinRequest);
            authResponse.setMessage("Автентифікація успішна");
            return new ResponseEntity<>(authResponse, HttpStatus.OK);
        }catch(RuntimeException e){
            AuthResponse authResponse = new AuthResponse();
            authResponse.setMessage(e.getMessage());
            return new ResponseEntity<>(authResponse, HttpStatus.BAD_REQUEST);
        }
    }
}
