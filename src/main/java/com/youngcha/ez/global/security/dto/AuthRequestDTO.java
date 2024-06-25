package com.youngcha.ez.global.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;

public class AuthRequestDTO {

    @Data
    @RequiredArgsConstructor
    public static class JoinDTO {

        @NotBlank(message = "이름을 입력해주세요.")
        @Size(min = 1, max = 10, message = "이름은 1~10자 이어야 합니다.")
        private String username;

        @NotBlank(message = "비밀번호를 입력해주세요.")
        @Size(min = 4, max = 12, message = "비밀번호는 1~12자 이어야 합니다.")
        private String password;

        @NotBlank(message = "비밀번호 확인을 입력해주세요.")
        @Size(min = 4, max = 12, message = "비밀번호 확인은 1~12자 이어야 합니다.")
        private String passwordCheck;

        @NotBlank(message = "아이디를 입력해주세요.")
        @Size(min = 1, max = 10, message = "아이디는 1~10자 이어야 합니다.")
        private String userId;

        @NotBlank(message = "이메일을 입력해주세요.")
        @Email(message = "이메일 형식으로 입력해주세요.")
        private String email;
    }
}
