package com.youngcha.ez.member.presentation;

import com.youngcha.ez.member.domain.dto.MemberRequest;
import com.youngcha.ez.member.domain.entity.Member;
import com.youngcha.ez.member.infrastructure.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/info")
    public ResponseEntity<?> userInfoUpdateForm() {

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        if(userId == null) {
            return new ResponseEntity<>("세션이 만료되었습니다.", HttpStatus.BAD_REQUEST);
        }

        MemberRequest.UserInfoDTO userInfoDTO = memberService.getUserInfo(userId);

        return new ResponseEntity<>(userInfoDTO, HttpStatus.OK);
    }

    @PutMapping("/info")
    public ResponseEntity<?> userInfoUpdate(@RequestBody @Valid MemberRequest.UserInfoUpdateDTO userInfoUpdateDTO)
            throws Exception {

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        if(userId == null) {
            return new ResponseEntity<>("세션이 만료되었습니다.", HttpStatus.BAD_REQUEST);
        }

        memberService.updateMemberInfo(userId, userInfoUpdateDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
