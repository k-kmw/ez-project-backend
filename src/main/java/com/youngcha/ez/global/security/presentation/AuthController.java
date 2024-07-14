package com.youngcha.ez.global.security.presentation;

import com.youngcha.ez.global.security.application.AuthService;
import com.youngcha.ez.global.security.domain.entity.RefreshToken;
import com.youngcha.ez.global.security.domain.repository.RefreshTokenRepository;
import com.youngcha.ez.global.security.dto.AuthRequestDTO;
import com.youngcha.ez.global.security.jwt.JwtUtil;
import com.youngcha.ez.member.domain.entity.Member;
import com.youngcha.ez.member.infrastructure.MemberService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@Controller
@RequestMapping("/user")
public class AuthController {

    private final AuthService authService;
    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService, MemberService memberService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/join")
    public ResponseEntity<Void> join(@RequestBody @Valid AuthRequestDTO.JoinDTO joinDTO) {

        authService.join(joinDTO);
        return ResponseEntity.status(200).build();
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        // get refresh token
        String refreshToken = getRefreshToken(request);

        if(refreshToken == null) {
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        // expired check
        if (authService.isTokenExpired(refreshToken)) {
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        // 토큰이 refresh인지 check
        if (!authService.isRefreshToken(refreshToken)) {
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        // DB에 저장된 토큰인지 확인
        if (!authService.isTokenInDB(refreshToken)) {
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        // cookie 갱신을 위해 member 조회
        Member loginMember = memberService.findById(jwtUtil.getUserId(refreshToken));

        // make new JWT
        String newAccessToken = authService.rotateJwt("access", refreshToken, loginMember.getUsername(), 10*60*1000L);
        String newRefreshToken = authService.rotateJwt("refresh", refreshToken, loginMember.getUsername(), 24*60*60*1000L);

        // DB에 기존 refresh 토큰 삭제 후 새 refresh 토큰 저장
        authService.deleteRefreshToken(refreshToken);
        authService.addRefreshToken(newRefreshToken);
        
        // 응답 설정
        response.setHeader("Authorization", "Bearer " + newAccessToken);
        response.addCookie(createEncodedCookie("refresh", newRefreshToken));
        response.addCookie(createEncodedCookie("username", loginMember.getUsername()));
        response.addCookie(createEncodedCookie("userId", loginMember.getUserId()));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private String getRefreshToken(HttpServletRequest request) {

        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie : cookies) {

            if(cookie.getName().equals("refresh")) {

                refreshToken = cookie.getValue();
            }
        }

        return refreshToken;
    }

    Cookie createEncodedCookie(String name, String value) {

        value = URLEncoder.encode(value, StandardCharsets.UTF_8);
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
