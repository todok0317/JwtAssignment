package com.example.jwtassignment.domain.auth.service;

import com.example.jwtassignment.common.error.BusinessException;
import com.example.jwtassignment.common.error.ExceptionCode;
import com.example.jwtassignment.common.jwt.JwtUtil;
import com.example.jwtassignment.domain.User.entity.User;
import com.example.jwtassignment.domain.User.repository.UserRepository;
import com.example.jwtassignment.domain.auth.dto.request.LoginRequestDto;
import com.example.jwtassignment.domain.auth.dto.request.SignUpRequestDto;
import com.example.jwtassignment.domain.auth.dto.response.LoginResponseDto;
import com.example.jwtassignment.domain.auth.dto.response.SignUpResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public SignUpResponseDto signUp(SignUpRequestDto requestDto) {

        if(userRepository.existsByUsername(requestDto.getUsername())) {
            throw new BusinessException(ExceptionCode.USER_ALREADY_EXISTS);
        }

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        User user = new User(requestDto.getUsername(), encodedPassword, requestDto.getNickname());

        User savedUser = userRepository.save(user);

        return new SignUpResponseDto(savedUser);
    }

    @Transactional
    public LoginResponseDto login(LoginRequestDto requestDto) {
        User user = userRepository.findByUsername(requestDto.getUsername())
            .orElseThrow(() -> new BusinessException(ExceptionCode.INVALID_CREDENTIALS));

        if(!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new BusinessException(ExceptionCode.INVALID_CREDENTIALS);
        }

        String token = jwtUtil.createToken(user.getId(), user.getUsername(), user.getRoles().toString());

        return new LoginResponseDto(token);
    }
}
