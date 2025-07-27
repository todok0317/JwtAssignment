package com.example.jwtassignment.common.jwt;

import com.example.jwtassignment.common.error.ExceptionCode;
import com.example.jwtassignment.common.error.JwtAuthenticationException;
import com.example.jwtassignment.common.security.CustomUserDetailsService;
import com.example.jwtassignment.common.security.CustomUserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String url = request.getRequestURI();
        if (url.equals("/signup") || url.equals("/login")) { // 변경된 부분
            filterChain.doFilter(request, response);
            return;
        }

        String bearerToken = request.getHeader("Authorization");

        if (bearerToken == null) {
            throw new JwtAuthenticationException(ExceptionCode.MISSING_TOKEN);
        }

        try {
            String token = jwtUtil.substringToken(bearerToken);
            Claims claims = jwtUtil.extractClaims(token);

            Long userId = Long.parseLong(claims.getSubject());

            CustomUserPrincipal userDetails = userDetailsService.loadUserById(userId);
            UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);

        } catch (SecurityException | MalformedJwtException e) {
            log.error("잘못된 JWT 서명", e);
            throw new JwtAuthenticationException(ExceptionCode.INVALID_JWT_SIGNATURE);
        } catch (ExpiredJwtException e) {
            log.error("JWT 만료", e);
            throw new JwtAuthenticationException(ExceptionCode.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT", e);
            throw new JwtAuthenticationException(ExceptionCode.UNSUPPORTED_TOKEN);
        } catch (IllegalArgumentException e) {
            log.error("잘못된 JWT 토큰", e);
            throw new JwtAuthenticationException(ExceptionCode.INVALID_TOKEN);
        } catch (Exception e) {
            log.error("JWT 처리 중 예외 발생", e);
            throw new JwtAuthenticationException(ExceptionCode.INTERNAL_ERROR);
        }
    }
}