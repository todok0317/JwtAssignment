package com.example.jwtassignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class JwtAssignmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(JwtAssignmentApplication.class, args);
    }

}
