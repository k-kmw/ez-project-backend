package com.youngcha.ez.global.security.presentation;

import com.youngcha.ez.global.security.application.AuthService;
import com.youngcha.ez.global.security.domain.entity.RefreshToken;
import com.youngcha.ez.global.security.domain.repository.RefreshTokenRepository;
import com.youngcha.ez.global.security.dto.AuthRequestDTO;
import com.youngcha.ez.global.security.jwt.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@Controller
@RequestMapping("/user")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthController(AuthService authService, JwtUtil jwtUtil, RefreshTokenRepository refreshTokenRepository) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @PostMapping("/join")
    public ResponseEntity<Void> join(@Valid AuthRequestDTO.JoinDTO joinDTO) {

        authService.join(joinDTO);
        return ResponseEntity.status(200).build();
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        // get refresh token
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie : cookies) {

            if(cookie.getName().equals("refresh")) {

                refreshToken = cookie.getValue();
            }
        }

        if(refreshToken == null) {

            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        // expired check
        try {
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {

            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        // 토큰이 refresh인지 check
        String type = jwtUtil.getType(refreshToken);
        if(!type.equals("refresh")) {

            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }
        
        // DB에 저장된 토큰인지 확인
        Boolean isExist = refreshTokenRepository.existsByTokenValue(refreshToken);
        if(!isExist) {
            
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        String userId = jwtUtil.getUserId(refreshToken);
        String role = jwtUtil.getRole(refreshToken);

        // make new JWT
        String newAccessToken = jwtUtil.createJwt("access", userId, role, 10*60*1000L);
        String newRefreshToken = jwtUtil.createJwt("refresh", userId, role, 24*60*60*1000L);
        String newAuthorization = "Bearer " + newAccessToken;

        // DB에 기존 refresh 토큰 삭제 후 새 refresh 토큰 저장
        refreshTokenRepository.deleteByTokenValue(refreshToken);
        addRefreshToken(userId, newRefreshToken, 24*60*60*1000L);

        // 응답 설정
        response.setHeader("Authorization", newAuthorization);
        response.addCookie(createCookie("refresh", newRefreshToken));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
//        cookie.setSecure(true);
        cookie.setHttpOnly(true);

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
