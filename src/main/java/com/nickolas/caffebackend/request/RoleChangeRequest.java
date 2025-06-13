package com.nickolas.caffebackend.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.nickolas.caffebackend.domain.Role;

/**
 * DTO для зміни ролі користувача.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleChangeRequest {
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Role is required")
    private Role role;
}
