package com.nickolas.caffebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.nickolas.caffebackend.model")
public class CaffeWebsiteBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(CaffeWebsiteBackendApplication.class, args);
    }
}
