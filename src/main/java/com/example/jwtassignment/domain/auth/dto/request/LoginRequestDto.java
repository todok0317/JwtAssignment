package com.example.jwtassignment.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(description = "로그인 요청 DTO")
@Getter
@RequiredArgsConstructor
public class LoginRequestDto {

    @Schema(description = "사용자명", example = "JIN HO", required = true)
    @NotBlank(message = "이름은 필수입니다.")
    private final String username;

    @Schema(description = "비밀번호", example = "12341234", required = true)
    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 6, message = "비밀번호는 최소 6자 이상이어야 합니다.")
    private final String password;

}
