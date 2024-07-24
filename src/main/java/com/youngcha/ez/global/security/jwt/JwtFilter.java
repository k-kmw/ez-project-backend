package com.youngcha.ez.global.security.jwt;

import com.youngcha.ez.global.error.ErrorCode;
import com.youngcha.ez.global.security.dto.MemberDetails;
import com.youngcha.ez.member.domain.entity.Member;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {

        this.jwtUtil = jwtUtil;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);

            return;
        }

        String accessToken = authorization.split(" ")[1];

        if (accessToken == null) {

            filterChain.doFilter(request, response);

            return;
        }

        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {

            handleException(response, ErrorCode.TOKEN_EXPIRED); // Token expired
            return;
        }

        // access token인지 확인
        String type = jwtUtil.getType(accessToken);
        if (!type.equals("access")) {

            handleException(response, ErrorCode.INVALID_ACCESS_TOKEN); // Invalid access token
            return;
        }

        String userId = jwtUtil.getUserId(accessToken);
        String role = jwtUtil.getRole(accessToken);

        Member member = Member.builder()
            .userId(userId)
            .password("temppassword")
            .role(role)
            .build();
        MemberDetails memberDetails = new MemberDetails(member);

        Authentication authToken = new UsernamePasswordAuthenticationToken(memberDetails, null,
            memberDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }

    private void handleException(HttpServletResponse response, ErrorCode errorCode)
        throws IOException {
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        writer.print("{\"error\": \"" + errorCode.getMessage() + "\"}");
        writer.flush();
    }
}
