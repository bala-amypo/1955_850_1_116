package com.example.demo;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@OpenAPIDefinition(
    info = @Info(
        title = "Contract Breach Penalty Calculator API",
        version = "1.0",
        description = "REST API for managing contracts and calculating breach penalties with MySQL database"
    )
)
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @GetMapping("/")
    public String home() {
        return "Welcome to Contract Breach Penalty Calculator! " +
               "Visit /swagger-ui.html for API documentation. MySQL database is configured.";
    }

    @GetMapping("/health")
    public String health() {
        return "Application is running successfully on port 8090 with MySQL database!";
    }
}