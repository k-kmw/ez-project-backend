package com.youngcha.ez.global.security.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.youngcha.ez.global.security.application.AuthService;
import com.youngcha.ez.global.security.dto.AuthRequestDTO;
import com.youngcha.ez.global.security.dto.AuthRequestDTO.JoinDTO;
import com.youngcha.ez.global.security.jwt.JwtUtil;
import com.youngcha.ez.member.domain.entity.Member;
import com.youngcha.ez.member.infrastructure.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import jakarta.servlet.http.Cookie;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @Mock
    private MemberService memberService;

    @Mock
    private JwtUtil jwtUtil;

    ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    public void JoinSuccessTest() throws Exception {
        JoinDTO joinDTO = JoinDTO.builder()
            .userId("testId")
            .password("1234")
            .passwordCheck("1234")
            .email("test@naver.com")
            .username("hong")
            .build();

        mockMvc.perform(post("/user/join")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(joinDTO)))
            .andExpect(status().isOk());

        verify(authService, times(1)).join(any(AuthRequestDTO.JoinDTO.class));
    }

    @Test
    public void ReissueSuccessTest() throws Exception {
        String refreshToken = "valid-refresh-token";
        String accessToken = "new-access-token";
        String newRefreshToken = "new-refresh-token";
        String username = "testUser";
        String userId = "testUserId";
        String email = "email@gmail.com";
        String password = "password";

        Member member = Member.builder()
            .userId(userId)
            .username(username)
            .email(email)
            .password(password)
            .build();

        when(authService.isTokenExpired(anyString())).thenReturn(false);
        when(authService.isRefreshToken(anyString())).thenReturn(true);
        when(authService.isTokenInDB(anyString())).thenReturn(true);
        when(jwtUtil.getUserId(refreshToken)).thenReturn(userId);
        when(memberService.findById(anyString())).thenReturn(member);
        when(authService.rotateJwt(eq("access"), anyString(), eq(username), anyLong())).thenReturn(
            accessToken);
        when(authService.rotateJwt(eq("refresh"), anyString(), eq(username), anyLong())).thenReturn(
            newRefreshToken);
        doNothing().when(authService).deleteRefreshToken(anyString());
        doNothing().when(authService).addRefreshToken(anyString());

        // Perform request and assert response
        mockMvc.perform(post("/user/reissue")
                .cookie(new Cookie("refresh", refreshToken)))
            .andExpect(status().isOk());

        // Verify interactions
        verify(authService, times(1)).rotateJwt("access", refreshToken, username, 10 * 60 * 1000L);
        verify(authService, times(1)).rotateJwt("refresh", refreshToken, username,
            24 * 60 * 60 * 1000L);
        verify(authService, times(1)).deleteRefreshToken(refreshToken);
        verify(authService, times(1)).addRefreshToken(newRefreshToken);
    }

    @Test
    public void ReissueFailureExpiredTokenTest() throws Exception {
        String refreshToken = "invalid-refresh-token";

        when(authService.isTokenExpired(anyString())).thenReturn(true);

        mockMvc.perform(post("/user/reissue")
                .cookie(new Cookie("refresh", refreshToken)))
            .andExpect(status().isBadRequest());

        verify(authService, times(0)).rotateJwt(anyString(), anyString(), anyString(), anyLong());
    }

    @Test
    public void ReissueFailureNotRefreshTokenTest() throws Exception {
        String refreshToken = "not-refresh-token";

        when(authService.isRefreshToken(anyString())).thenReturn(false);

        mockMvc.perform(post("/user/reissue")
                .cookie(new Cookie("refresh", refreshToken)))
            .andExpect(status().isBadRequest());

        verify(authService, times(0)).rotateJwt(anyString(), anyString(), anyString(), anyLong());
    }

    @Test
    public void ReissueFailureNotRefreshTokenInDBTest() throws Exception {
        String refreshToken = "not-in-db-refresh-token";

        when(authService.isTokenInDB(anyString())).thenReturn(false);

        mockMvc.perform(post("/user/reissue")
                .cookie(new Cookie("refresh", refreshToken)))
            .andExpect(status().isBadRequest());

        verify(authService, times(0)).rotateJwt(anyString(), anyString(), anyString(), anyLong());
    }
}
