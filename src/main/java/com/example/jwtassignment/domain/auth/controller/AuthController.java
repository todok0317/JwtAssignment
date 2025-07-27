package com.example.jwtassignment.domain.auth.controller;

import com.example.jwtassignment.domain.auth.dto.request.LoginRequestDto;
import com.example.jwtassignment.domain.auth.dto.request.SignUpRequestDto;
import com.example.jwtassignment.domain.auth.dto.response.LoginResponseDto;
import com.example.jwtassignment.domain.auth.dto.response.SignUpResponseDto;
import com.example.jwtassignment.domain.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "인증 API", description = "회원가입 및 로그인 관련 API")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(
        summary = "회원가입", 
        description = "새로운 사용자를 등록합니다. 기본적으로 USER 권한이 부여됩니다."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "회원가입 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SignUpResponseDto.class),
                examples = @ExampleObject(
                    name = "회원가입 성공",
                    value = """
                        {
                          "username": "JIN HO",
                          "nickname": "Mentos",
                          "roles": [
                            {
                              "role": "USER"
                            }
                          ]
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "409", 
            description = "이미 존재하는 사용자명",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "중복 사용자 에러",
                    value = """
                        {
                          "error": {
                            "code": "USER_ALREADY_EXISTS",
                            "message": "이미 가입된 사용자입니다."
                          }
                        }
                        """
                )
            )
        )
    })
    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDto> signUp(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "회원가입 정보",
            content = @Content(
                schema = @Schema(implementation = SignUpRequestDto.class),
                examples = @ExampleObject(
                    name = "회원가입 요청",
                    value = """
                        {
                          "username": "JIN HO",
                          "password": "12341234",
                          "nickname": "Mentos"
                        }
                        """
                )
            )
        )
        @Valid @RequestBody SignUpRequestDto requestDto
    ) {
        SignUpResponseDto responseDto = authService.signUp(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(
        summary = "로그인", 
        description = "사용자 인증 후 JWT 토큰을 발급합니다. 발급된 토큰은 Authorization 헤더에 'Bearer {token}' 형식으로 사용하세요."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "로그인 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = LoginResponseDto.class),
                examples = @ExampleObject(
                    name = "로그인 성공",
                    value = """
                        {
                          "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwidXNlcm5hbWUi..."
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "인증 실패",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "로그인 실패",
                    value = """
                        {
                          "error": {
                            "code": "INVALID_CREDENTIALS",
                            "message": "아이디 또는 비밀번호가 올바르지 않습니다."
                          }
                        }
                        """
                )
            )
        )
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "로그인 정보",
            content = @Content(
                schema = @Schema(implementation = LoginRequestDto.class),
                examples = @ExampleObject(
                    name = "로그인 요청",
                    value = """
                        {
                          "username": "JIN HO",
                          "password": "12341234"
                        }
                        """
                )
            )
        )
        @Valid @RequestBody LoginRequestDto requestDto
    ) {
        LoginResponseDto responseDto = authService.login(requestDto);
        return ResponseEntity.ok(responseDto);
    }
}