package com.youngcha.ez.global.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

public class AuthResponseDTO {

    @Data
    @Builder
    public static class LoginDTO {

        private String username;

        private String userId;

        private String email;
    }
}
