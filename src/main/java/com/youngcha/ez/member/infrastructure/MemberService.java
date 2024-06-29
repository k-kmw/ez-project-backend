package com.youngcha.ez.member.infrastructure;

import com.youngcha.ez.member.domain.dto.MemberRequest;
import com.youngcha.ez.member.domain.dto.MemberRequest.UserInfoUpdateDTO;
import com.youngcha.ez.member.domain.entity.Member;
import com.youngcha.ez.member.domain.repository.MemberRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberRequest.UserInfoDTO getUserInfo(String userId) {

        Member member = memberRepository.findByUserId(userId);

        return MemberRequest.UserInfoDTO
                .builder()
                .userId(member.getUserId())
                .username(member.getUsername())
                .email(member.getEmail())
                .build();
    }

    @Transactional
    public void updateMemberInfo(String userId, UserInfoUpdateDTO updateDTO) throws Exception {

        Member member = memberRepository.findByUserId(userId);

        member.updateInfo(updateDTO);
    }
}
