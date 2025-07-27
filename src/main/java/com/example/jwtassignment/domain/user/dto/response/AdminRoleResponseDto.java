package com.example.jwtassignment.domain.user.dto.response;

import com.example.jwtassignment.domain.user.entity.User;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminRoleResponseDto {
    private String username;
    private String nickname;
    private List<RoleDto> roles;

    @Getter
    @AllArgsConstructor
    public static class RoleDto {
        private String role;
    }

    public AdminRoleResponseDto(User user) {
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.roles = user.getRoles().stream()
            .map(role -> new RoleDto(role.name()))
            .collect(Collectors.toList());
    }
}
