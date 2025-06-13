package com.nickolas.caffebackend.domain;

/**
 * Ролі користувачів у системі.
 */
public enum Role {
    /** Адміністратор з повним доступом. */
    ROLE_ADMIN,
    /** Звичайний користувач (клієнт). */
    ROLE_USER,
    ROLE_WORKER
}
