package com.example.shopofsoks;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition
public class ShopOfSoksApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopOfSoksApplication.class, args);
    }

}
