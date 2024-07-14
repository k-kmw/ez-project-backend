package com.youngcha.ez.global.security.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.youngcha.ez.global.security.domain.entity.RefreshToken;
import com.youngcha.ez.global.security.domain.repository.RefreshTokenRepository;
import com.youngcha.ez.global.security.dto.AuthResponseDTO;
import com.youngcha.ez.global.security.dto.MemberDetails;
import com.youngcha.ez.member.domain.entity.Member;
import com.youngcha.ez.member.domain.repository.MemberRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;

    public LoginFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
            RefreshTokenRepository refreshTokenRepository, MemberRepository memberRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
        this.memberRepository = memberRepository;
        setFilterProcessesUrl("/user/login");
        setUsernameParameter("userId");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException {

        String userId = obtainUsername(request);
        String password = obtainPassword(request);

        System.out.println(userId);
        System.out.println(password);

        // username과 password를 검증하기 위해 token에 담기
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userId, password);

        // 검증을 위해 token을 AuthenticationManager로 전달
        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
            HttpServletResponse response, FilterChain chain, Authentication authentication)
            throws IOException {

        // 유저 정보
        String userId = authentication.getName();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        // token 생성
        String accessToken = jwtUtil.createJwt("access", userId, role, 10 * 60 * 1000L);
        String refreshToken = jwtUtil.createJwt("refresh", userId, role, 24 * 60 * 60 * 1000L);

        // refresh 토큰 DB 저장
        addRefreshToken(userId, refreshToken, 24 * 60 * 60 * 1000L);

        // 로그인 member 조회
        Member loginMember = memberRepository.findByUserId(userId);
        AuthResponseDTO.LoginDTO loginDTO = AuthResponseDTO.LoginDTO.builder()
                .userId(userId)
                .username(loginMember.getUsername())
                .email(loginMember.getEmail())
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        String loginDTOJson = objectMapper.writeValueAsString(loginDTO);

        // 응답 설정
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Authorization", "Bearer " + accessToken);
        response.addCookie(createEncodedCookie(true, "refresh", refreshToken));
        response.addCookie(createEncodedCookie(false, "username", loginMember.getUsername()));
        response.addCookie(createEncodedCookie(false, "userId", loginMember.getUserId()));
        response.getWriter().write(loginDTOJson);
        response.setStatus(HttpStatus.OK.value());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
            HttpServletResponse response, AuthenticationException failed) {

        response.setStatus(401);
    }

    Cookie createEncodedCookie(boolean isHttpOnly, String name, String value) {
        value = URLEncoder.encode(value, StandardCharsets.UTF_8);
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setDomain("");
        cookie.setPath("/");
        cookie.setHttpOnly(isHttpOnly);
        cookie.setAttribute("SameSite", "None");
        cookie.setSecure(true);
        return cookie;
    }

    private void addRefreshToken(String userId, String tokenValue, long expiredMs) {

        Date expirationDate = new Date(System.currentTimeMillis() + expiredMs);

        RefreshToken refreshToken = RefreshToken.builder()
                .userId(userId)
                .tokenValue(tokenValue)
                .expiration(expirationDate.toString())
                .build();

        refreshTokenRepository.save(refreshToken);
    }
}
