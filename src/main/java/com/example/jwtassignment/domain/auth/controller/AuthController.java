package com.example.jwtassignment.domain.auth.controller;

import com.example.jwtassignment.domain.auth.dto.request.LoginRequestDto;
import com.example.jwtassignment.domain.auth.dto.request.SignUpRequestDto;
import com.example.jwtassignment.domain.auth.dto.response.LoginResponseDto;
import com.example.jwtassignment.domain.auth.dto.response.SignUpResponseDto;
import com.example.jwtassignment.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDto> signUp (@Valid @RequestBody SignUpRequestDto requestDto) {
        SignUpResponseDto responseDto = authService.signUp(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login (
        @Valid @RequestBody LoginRequestDto requestDto) {
        LoginResponseDto responseDto = authService.login(requestDto);
        return ResponseEntity.ok(responseDto);
    }

}
