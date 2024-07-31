package com.youngcha.ez.global.security.application;

import com.youngcha.ez.global.error.ErrorCode;
import com.youngcha.ez.global.error.exception.BusinessException;
import com.youngcha.ez.global.security.dto.MemberDetails;
import com.youngcha.ez.global.security.jwt.CustomFilterExceptionHandler;
import com.youngcha.ez.member.domain.entity.Member;
import com.youngcha.ez.member.domain.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public MemberDetailsService(MemberRepository memberRepository) {

        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userId) {

        Member member = memberRepository.findByUserId(userId);

        if(member == null) {
            throw new RuntimeException("user를 찾을 수 없습니다. userId: " + userId);
        }

        return new MemberDetails(member);
    }
}
