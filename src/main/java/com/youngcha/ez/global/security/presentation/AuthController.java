package com.youngcha.ez.global.security.presentation;

import com.youngcha.ez.global.security.application.AuthService;
import com.youngcha.ez.global.security.dto.AuthRequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/join")
    public ResponseEntity<Void> join(@Valid AuthRequestDTO.JoinDTO joinDTO) {

        authService.join(joinDTO);
        return ResponseEntity.status(200).build();
    }
}
