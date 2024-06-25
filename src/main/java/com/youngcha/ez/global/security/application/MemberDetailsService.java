package com.youngcha.ez.global.security.application;

import com.youngcha.ez.global.security.dto.MemberDetails;
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
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

        Member member = memberRepository.findByUserId(userId);

        if(member == null) { // TODO: id에 해당하는 멤버가 없으면 403 예외 처리
            System.out.println("일치하는 id를 조회할 수 없습니다.");
            return null;
        }

        return new MemberDetails(member);
    }
}
