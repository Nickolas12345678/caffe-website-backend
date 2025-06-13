package com.nickolas.caffebackend.domain;

/**
 * Статус замовлення, який відображає поточний етап обробки.
 */
public enum OrderStatus {
    /** Замовлення очікує обробки. */
    PENDING,
    /** Замовлення в процесі приготування або доставки. */
    IN_PROGRESS,
    /** Замовлення завершено. */
    COMPLETED,
    /** Замовлення скасовано. */
    CANCELLED
}
