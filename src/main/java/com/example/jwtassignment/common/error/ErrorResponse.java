package com.example.jwtassignment.common.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private ErrorDetail error;

    public ErrorResponse(String code, String message) {
        this.error = new ErrorDetail(code, message);
    }

    @Data
    @AllArgsConstructor
    static class ErrorDetail {
        private String code;
        private String message;
    }
}

