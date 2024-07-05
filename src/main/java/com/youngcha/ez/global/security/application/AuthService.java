package com.youngcha.ez.global.security.application;

import com.youngcha.ez.global.security.domain.entity.RefreshToken;
import com.youngcha.ez.global.security.domain.repository.RefreshTokenRepository;
import com.youngcha.ez.global.security.dto.AuthRequestDTO;
import com.youngcha.ez.global.security.jwt.JwtUtil;
import com.youngcha.ez.member.domain.entity.Member;
import com.youngcha.ez.member.domain.repository.MemberRepository;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthService(MemberRepository memberRepository, BCryptPasswordEncoder bCryptPasswordEncoder, JwtUtil jwtUtil, RefreshTokenRepository refreshTokenRepository) {
        this.memberRepository = memberRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    public void join(AuthRequestDTO.JoinDTO joinDTO) {

        boolean isExist = memberRepository.existsByUserId(joinDTO.getUserId());

        if(isExist) { // TODO: 이미 가입된 회원일 경우 예외 처리
            return;
        }

        if(!joinDTO.getPassword().equals(joinDTO.getPasswordCheck())) { // TODO: 비밀번호와 비밀번호 확인이 다를 경우 예외 처리
            return;
        }

        Member member = Member.builder()
                .userId(joinDTO.getUserId())
                .password(bCryptPasswordEncoder.encode(joinDTO.getPassword()))
                .username(joinDTO.getUsername())
                .email(joinDTO.getEmail())
                .role("ROLE_USER")
                .build();

        System.out.println(member.getPassword());
        memberRepository.save(member);
    }

    public boolean isTokenExpired(String refreshToken) {

        try {
            jwtUtil.isExpired(refreshToken);
            return false;
        } catch (ExpiredJwtException e) {

            return true;
        }
    }

    public boolean isRefreshToken(String refreshToken) {

        String type = jwtUtil.getType(refreshToken);
        return type.equals("refresh");
    }

    public boolean isTokenInDB(String refreshToken) {

        return refreshTokenRepository.existsByTokenValue(refreshToken);
    }

    public String rotateJwt(String type, String refreshToken, Long expiredMs) {

        String userId = jwtUtil.getUserId(refreshToken);
        String role = jwtUtil.getRole(refreshToken);

        return jwtUtil.createJwt(type, userId, role, expiredMs);
    }

    @Transactional
    public void deleteRefreshToken(String refreshToken) {

        refreshTokenRepository.deleteByTokenValue(refreshToken);
    }

    @Transactional
    public void addRefreshToken(String token) {

        String userId = jwtUtil.getUserId(token);
        Date expirationDate = new Date(System.currentTimeMillis() + 24*60*60*1000L);

        RefreshToken refreshToken = RefreshToken.builder()
                .userId(userId)
                .tokenValue(token)
                .expiration(expirationDate.toString())
                .build();

        refreshTokenRepository.save(refreshToken);
    }
}
