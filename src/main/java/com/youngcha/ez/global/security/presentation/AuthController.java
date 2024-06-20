package com.youngcha.ez.global.security.presentation;

import com.youngcha.ez.global.security.application.AuthService;
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

@Controller
@RequestMapping("/user")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
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

        String userId = jwtUtil.getUserId(refreshToken);
        String role = jwtUtil.getRole(refreshToken);

        // make new JWT
        String newAccessToken = jwtUtil.createJwt("access", userId, role, 10*60*1000L);
        String newAuthorization = "Bearer " + newAccessToken;


        response.setHeader("Authorization", newAuthorization);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
