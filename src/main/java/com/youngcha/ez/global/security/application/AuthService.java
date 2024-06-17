package com.youngcha.ez.global.security.application;

import com.youngcha.ez.global.security.dto.AuthRequestDTO;
import com.youngcha.ez.member.domain.entity.Member;
import com.youngcha.ez.member.domain.repository.MemberRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthService(MemberRepository memberRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.memberRepository = memberRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

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
                .password(joinDTO.getPassword())
                .username(joinDTO.getUsername())
                .email(joinDTO.getEmail())
                .build();

        memberRepository.save(member);
    }
}
