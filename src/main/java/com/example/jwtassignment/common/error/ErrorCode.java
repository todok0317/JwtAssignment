package com.example.jwtassignment.common.error;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    HttpStatus getHttpStatus();
    String getMessage();
    String name(); // enum의 name() 메서드
}
