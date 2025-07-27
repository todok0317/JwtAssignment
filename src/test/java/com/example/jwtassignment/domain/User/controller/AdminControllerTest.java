package com.example.jwtassignment.domain.User.controller;

import com.example.jwtassignment.common.jwt.JwtUtil;
import com.example.jwtassignment.domain.User.entity.User;
import com.example.jwtassignment.domain.User.enums.UserRole;
import com.example.jwtassignment.domain.User.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("관리자 권한 부여 성공 - 관리자가 요청")
    void grantAdminRole_Success_ByAdmin() throws Exception {
        // given
        // 관리자 사용자 생성
        User adminUser = User.builder()
            .username("admin")
            .password(passwordEncoder.encode("password123"))
            .nickname("관리자")
            .roles(new ArrayList<>(List.of(UserRole.ADMIN)))
            .build();
        User savedAdmin = userRepository.save(adminUser);

        // 일반 사용자 생성
        User normalUser = User.builder()
            .username("normaluser")
            .password(passwordEncoder.encode("password123"))
            .nickname("일반유저")
            .roles(new ArrayList<>(List.of(UserRole.ADMIN)))
            .build();
        User savedNormalUser = userRepository.save(normalUser);

        // JWT 토큰 생성
        String adminToken = jwtUtil.createToken(savedAdmin.getId(), savedAdmin.getUsername(),
            savedAdmin.getRoles().toString());

        // when & then
        mockMvc.perform(patch("/admin/users/{userId}/roles", savedNormalUser.getId())
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value(savedNormalUser.getUsername()));
    }

    @Test
    @DisplayName("관리자 권한 부여 실패 - 일반 사용자가 요청")
    void grantAdminRole_Fail_ByNormalUser() throws Exception {
        // given
        // 일반 사용자 1 생성
        User normalUser1 = User.builder()
            .username("normaluser1")
            .password(passwordEncoder.encode("password123"))
            .nickname("일반유저1")
            .roles(new ArrayList<>(List.of(UserRole.USER)))
            .build();
        User savedNormalUser1 = userRepository.save(normalUser1);

        // 일반 사용자 2 생성
        User normalUser2 = User.builder()
            .username("normaluser2")
            .password(passwordEncoder.encode("password123"))
            .nickname("일반유저2")
            .roles(new ArrayList<>(List.of(UserRole.USER)))
            .build();
        User savedNormalUser2 = userRepository.save(normalUser2);

        // 일반 사용자의 JWT 토큰 생성
        String userToken = jwtUtil.createToken(savedNormalUser1.getId(),
            savedNormalUser1.getUsername(), savedNormalUser1.getRoles().toString());

        // when & then
        mockMvc.perform(patch("/admin/users/{userId}/roles", savedNormalUser2.getId())
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("관리자 권한 부여 실패 - 존재하지 않는 사용자")
    void grantAdminRole_Fail_UserNotFound() throws Exception {
        // given
        User adminUser = User.builder()
            .username("admin2")
            .password(passwordEncoder.encode("password123"))
            .nickname("관리자2")
            .roles(new ArrayList<>(List.of(UserRole.ADMIN)))
            .build();
        User savedAdmin = userRepository.save(adminUser);

        String adminToken = jwtUtil.createToken(savedAdmin.getId(), savedAdmin.getUsername(),
            savedAdmin.getRoles().toString());

        // when & then
        mockMvc.perform(patch("/admin/users/{userId}/roles", 999L) // 존재하지 않는 ID
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("관리자 권한 부여 실패 - 인증 토큰 없음")
    void grantAdminRole_Fail_NoToken() throws Exception {
        // given
        User normalUser = User.builder()
            .username("normaluser3")
            .password(passwordEncoder.encode("password123"))
            .nickname("일반유저3")
            .roles(new ArrayList<>(List.of(UserRole.USER)))
            .build();
        User savedNormalUser = userRepository.save(normalUser);

        // when & then
        mockMvc.perform(patch("/admin/users/{userId}/roles", savedNormalUser.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("관리자 권한 부여 실패 - 유효하지 않은 토큰")
    void grantAdminRole_Fail_InvalidToken() throws Exception {
        // given
        User normalUser = User.builder()
            .username("normaluser4")
            .password(passwordEncoder.encode("password123"))
            .nickname("일반유저4")
            .roles(new ArrayList<>(List.of(UserRole.USER)))
            .build();
        User savedNormalUser = userRepository.save(normalUser);

        // JWT 형식은 맞지만 서명이 잘못된 토큰
        String invalidToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjMiLCJpYXQiOjE2MzIzMDcyMDB9.wrongsignature";

        // when & then
        mockMvc.perform(patch("/admin/users/{userId}/roles", savedNormalUser.getId())
                .header("Authorization", "Bearer " + invalidToken)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }
}
