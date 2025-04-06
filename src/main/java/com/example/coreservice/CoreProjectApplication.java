package com.example.coreservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.example")
public class CoreProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(CoreProjectApplication.class, args);
    }
}
