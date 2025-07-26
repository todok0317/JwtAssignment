package com.example.jwtassignment.domain.auth.dto.response;

import com.example.jwtassignment.domain.User.entity.User;
import com.example.jwtassignment.domain.User.enums.UserRole;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class SignUpResponseDto {

    private final String username;
    private final String nickname;
    private final List<RoleDto> roles;

    @Getter
    @AllArgsConstructor
    public static class RoleDto {
        private String role;
    }

    public SignUpResponseDto(User user) {
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.roles = user.getRoles().stream()
            .map(role -> new RoleDto(role.name()))
            .collect(Collectors.toList());
    }
}


