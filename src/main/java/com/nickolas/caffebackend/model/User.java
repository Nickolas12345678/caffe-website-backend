package com.nickolas.caffebackend.model;

import com.nickolas.caffebackend.domain.Role;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Представляє зареєстрованого користувача системи.
 * Реалізує інтерфейс {@link UserDetails} для інтеграції зі Spring Security.
 */
@Entity
@Table(name = "users")
@Data
public class User implements UserDetails {

    /** Унікальний ідентифікатор користувача. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Ім'я користувача. */
    private String username;

    /** Email користувача. */
    private String email;

    /** Хеш пароля. */
    private String password;

    /** Роль користувача. */
    @Enumerated(EnumType.STRING)
    private Role role;

    /**
     * Повертає список прав доступу (ролей), що призначені користувачу.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
