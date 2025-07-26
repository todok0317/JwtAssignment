package com.example.jwtassignment.domain.User.enums;

import java.util.Arrays;

public enum UserRole {
    USER, ADMIN;

    public static UserRole of(String role) {
        return Arrays.stream(UserRole.values())
            .filter(r -> r.name().equalsIgnoreCase(role))
            .findFirst()
            .orElseThrow(() ->  new IllegalArgumentException("올바르지 않은 UserRole입니다: " + role));
    }
}
