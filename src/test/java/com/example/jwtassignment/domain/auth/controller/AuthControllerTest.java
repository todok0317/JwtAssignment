package com.example.jwtassignment.domain.auth.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.jwtassignment.domain.User.entity.User;
import com.example.jwtassignment.domain.User.repository.UserRepository;
import com.example.jwtassignment.domain.auth.dto.request.LoginRequestDto;
import com.example.jwtassignment.domain.auth.dto.request.SignUpRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureWebMvc
@Transactional
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(springSecurity())
            .build();
    }

    @Test
    @DisplayName("회원가입 성공")
    void signUp_Success() throws Exception {
        // given
        SignUpRequestDto signUpRequest = new SignUpRequestDto(
            "testuser",
            "password123",
            "testnick"
        );

        // when & then
        mockMvc.perform(post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.username").value("testuser"))
            .andExpect(jsonPath("$.nickname").value("testnick"))
            .andExpect(jsonPath("$.roles[0].role").value("USER"));
    }

    @Test
    @DisplayName("회원가입 실패 - 중복된 사용자명")
    void signUp_Fail_DuplicateUsername() throws Exception {
        // given
        User existingUser = new User("testuser", "encodedPassword", "nickname");
        userRepository.save(existingUser);

        SignUpRequestDto signUpRequest = new SignUpRequestDto(
            "testuser",
            "password123",
            "testnick"
        );

        // when & then
        mockMvc.perform(post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest)))
            .andDo(print())
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.error.code").value("USER_ALREADY_EXISTS"))
            .andExpect(jsonPath("$.error.message").value("이미 가입된 사용자입니다."));
    }

    @Test
    @DisplayName("회원가입 실패 - 유효하지 않은 입력값")
    void signUp_Fail_InvalidInput() throws Exception {
        // given
        SignUpRequestDto signUpRequest = new SignUpRequestDto(
            "",  // 빈 사용자명
            "123", // 너무 짧은 비밀번호
            ""   // 빈 닉네임
        );

        // when & then
        mockMvc.perform(post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest)))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("로그인 성공")
    void login_Success() throws Exception {
        // given
        String rawPassword = "password123";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        User user = new User("testuser", encodedPassword, "testnick");
        userRepository.save(user);

        LoginRequestDto loginRequest = new LoginRequestDto(
            "testuser",
            rawPassword
        );

        // when & then
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").exists())
            .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    @DisplayName("로그인 실패 - 존재하지 않는 사용자")
    void login_Fail_UserNotFound() throws Exception {
        // given
        LoginRequestDto loginRequest = new LoginRequestDto(
            "nonexistentuser",
            "password123"
        );

        // when & then
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andDo(print())
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.error.code").value("INVALID_CREDENTIALS"))
            .andExpect(jsonPath("$.error.message").value("아이디 또는 비밀번호가 올바르지 않습니다."));
    }

    @Test
    @DisplayName("로그인 실패 - 잘못된 비밀번호")
    void login_Fail_WrongPassword() throws Exception {
        // given
        String rawPassword = "password123";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        User user = new User("testuser", encodedPassword, "testnick");
        userRepository.save(user);

        LoginRequestDto loginRequest = new LoginRequestDto(
            "testuser",
            "wrongpassword"
        );

        // when & then
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andDo(print())
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.error.code").value("INVALID_CREDENTIALS"))
            .andExpect(jsonPath("$.error.message").value("아이디 또는 비밀번호가 올바르지 않습니다."));
    }

    @Test
    @DisplayName("로그인 실패 - 유효하지 않은 입력값")
    void login_Fail_InvalidInput() throws Exception {
        // given
        LoginRequestDto loginRequest = new LoginRequestDto(
            "",  // 빈 사용자명
            ""   // 빈 비밀번호
        );

        // when & then
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }
}
