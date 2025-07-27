package com.example.jwtassignment.domain.User.controller;

import com.example.jwtassignment.common.security.CustomUserPrincipal;
import com.example.jwtassignment.domain.User.dto.response.AdminRoleResponseDto;
import com.example.jwtassignment.domain.User.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PatchMapping("/admin/users/{userId}/roles")
    public ResponseEntity<AdminRoleResponseDto> grantAdminRole(@PathVariable Long userId) {
        AdminRoleResponseDto responseDto = adminService.grantAdminRole(userId);
        return ResponseEntity.ok(responseDto);
    }


}
