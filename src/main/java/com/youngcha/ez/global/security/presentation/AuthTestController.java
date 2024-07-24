package com.youngcha.ez.global.security.presentation;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;
import java.util.Iterator;

@Controller
public class AuthTestController {

    @GetMapping("/test")
    @ResponseBody
    public String accessTestWithJWT() {

        // jwt는 stateless로 관리되지만, 요청에 대하여 세션을 생성하고 응답할 때까지 유지하기 때문에 사용자 정보에 접근 가능
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
        GrantedAuthority auth = iter.next();
        String role = auth.getAuthority();

        return "ok " + userId + " " + role;
    }

    @GetMapping("/")
    @ResponseBody
    public String rootWithoutAccessToken() {

        return "ok ";
    }
}
