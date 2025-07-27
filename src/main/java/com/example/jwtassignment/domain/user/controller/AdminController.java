package com.example.jwtassignment.domain.user.controller;

import com.example.jwtassignment.domain.user.dto.response.AdminRoleResponseDto;
import com.example.jwtassignment.domain.user.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "관리자 API", description = "관리자 권한이 필요한 API")
@RestController
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @Operation(
        summary = "사용자 관리자 권한 부여", 
        description = "특정 사용자에게 ADMIN 권한을 부여합니다. 요청자는 ADMIN 권한이 필요합니다.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "관리자 권한 부여 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AdminRoleResponseDto.class),
                examples = @ExampleObject(
                    name = "권한 부여 성공",
                    value = """
                        {
                          "username": "JIN HO",
                          "nickname": "Mentos",
                          "roles": [
                            {
                              "role": "ADMIN"
                            }
                          ]
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "인증되지 않은 요청",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "인증 실패",
                    value = """
                        {
                          "error": {
                            "code": "INVALID_TOKEN",
                            "message": "유효하지 않은 인증 토큰입니다."
                          }
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "403", 
            description = "접근 권한 부족 (ADMIN 권한 필요)",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "권한 부족",
                    value = """
                        {
                          "error": {
                            "code": "ACCESS_DENIED",
                            "message": "접근 권한이 없습니다."
                          }
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "사용자를 찾을 수 없음",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "사용자 없음",
                    value = """
                        {
                          "error": {
                            "code": "USER_NOT_FOUND",
                            "message": "사용자를 찾을 수 없습니다."
                          }
                        }
                        """
                )
            )
        )
    })
    @PatchMapping("/admin/users/{userId}/roles")
    public ResponseEntity<AdminRoleResponseDto> grantAdminRole(
        @Parameter(description = "관리자 권한을 부여할 사용자 ID", example = "15")
        @PathVariable Long userId
    ) {
        AdminRoleResponseDto responseDto = adminService.grantAdminRole(userId);
        return ResponseEntity.ok(responseDto);
    }
}