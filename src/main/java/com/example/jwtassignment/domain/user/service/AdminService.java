package com.example.jwtassignment.domain.user.service;

import com.example.jwtassignment.common.error.BusinessException;
import com.example.jwtassignment.common.error.ExceptionCode;
import com.example.jwtassignment.domain.user.dto.response.AdminRoleResponseDto;
import com.example.jwtassignment.domain.user.entity.User;
import com.example.jwtassignment.domain.user.enums.UserRole;
import com.example.jwtassignment.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    public AdminRoleResponseDto grantAdminRole(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(()-> new BusinessException(ExceptionCode.USER_NOT_FOUND));

        if(!user.getRoles().contains(UserRole.ADMIN)) {
            user.addRole(UserRole.ADMIN);
            userRepository.save(user);
        }

        return new AdminRoleResponseDto(user);
    }

}
