package com.youngcha.ez.member.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

public class MemberRequest {

    @Data
    @Builder
    public static class UserInfoDTO {

        private String username;
        private String userId;
        private String email;
    }

    @Data
    public static class UserInfoUpdateDTO {

        @NotBlank(message = "이름을 입력해주세요.")
        @Size(min = 1, max = 10, message = "이름은 1~10자 이어야 합니다.")
        private String username;

        @NotBlank(message = "비밀번호를 입력해주세요.")
        @Size(min = 4, max = 12, message = "비밀번호는 1~12자 이어야 합니다.")
        private String password;

        @NotBlank(message = "비밀번호 확인을 입력해주세요.")
        @Size(min = 4, max = 12, message = "비밀번호 확인은 1~12자 이어야 합니다.")
        private String passwordCheck;

        @NotBlank(message = "이메일을 입력해주세요.")
        @Email(message = "이메일 형식으로 입력해주세요.")
        private String email;

        private String profileImageUrl;
    }
}
