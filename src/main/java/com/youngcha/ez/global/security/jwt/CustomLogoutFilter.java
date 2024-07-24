package com.youngcha.ez.global.security.jwt;

import com.youngcha.ez.global.error.ErrorCode;
import com.youngcha.ez.global.security.domain.repository.RefreshTokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class CustomLogoutFilter extends GenericFilterBean {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public CustomLogoutFilter(JwtUtil jwtUtil, RefreshTokenRepository refreshTokenRepository) {

        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        // path and method verify
        String requestUri = request.getRequestURI();
        if (!requestUri.matches("^/user/logout$")) {

            filterChain.doFilter(request, response);
            return;
        }
        String requestMethod = request.getMethod();
        if (!requestMethod.equals("POST")) {

            filterChain.doFilter(request, response);
            return;
        }

        // get refresh token
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("refresh")) {

                refreshToken = cookie.getValue();
            }
        }

        // refresh null check
        if (refreshToken == null) {

            CustomFilterExceptionHandler.handleException(response, ErrorCode.REFRESH_TOKEN_NOT_FOUND);
            return;
        }

        // expired check
        try {
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {

            CustomFilterExceptionHandler.handleException(response, ErrorCode.REFRESH_TOKEN_EXPIRED);
            return;
        }

        // 토큰이 refresh인지 확인
        String type = jwtUtil.getType(refreshToken);
        if (!type.equals("refresh")) {

            CustomFilterExceptionHandler.handleException(response, ErrorCode.INVALID_REFRESH_TOKEN);
            return;
        }
        
        // DB에 저장되어 있는지 확인
        Boolean isExist = refreshTokenRepository.existsByTokenValue(refreshToken);
        if (!isExist) {

            CustomFilterExceptionHandler.handleException(response, ErrorCode.INVALID_REFRESH_TOKEN);
            return;
        }

        // 로그아웃 진행: DB에서 refresh 토큰 제거
        refreshTokenRepository.deleteByTokenValue(refreshToken);

        // 로그인 user에 관한 쿠키 제거
        deleteLoginUserCookies(response);
        
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private void deleteLoginUserCookies(HttpServletResponse response) {

        deleteCookie("refresh", response);
        deleteCookie("username", response);
        deleteCookie("userId", response);
    }

    private void deleteCookie(String key, HttpServletResponse response) {

        Cookie cookie = new Cookie(key, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
