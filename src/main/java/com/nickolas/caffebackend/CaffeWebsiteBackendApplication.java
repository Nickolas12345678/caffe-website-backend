package com.nickolas.caffebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

/**
 * Головний клас запуску Spring Boot застосунку для Caffe Website.
 * <p>
 * Ініціалізує контекст застосунку та запускає сервер.
 */
@SpringBootApplication
@EntityScan(basePackages = "com.nickolas.caffebackend.model")
public class CaffeWebsiteBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(CaffeWebsiteBackendApplication.class, args);
    }
}
