package com.youngcha.ez.global.security.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AuthTestController {

    @GetMapping("/test")
    @ResponseBody
    public String accessTestWithJWT() {

        return "ok";
    }
}
