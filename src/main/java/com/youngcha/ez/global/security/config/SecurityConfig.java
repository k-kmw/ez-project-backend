package com.youngcha.ez.global.security.config;

import com.youngcha.ez.global.security.domain.repository.RefreshTokenRepository;
import com.youngcha.ez.global.security.jwt.CustomAuthenticationEntryPoint;
import com.youngcha.ez.global.security.jwt.JwtFilter;
import com.youngcha.ez.global.security.jwt.JwtUtil;
import com.youngcha.ez.global.security.jwt.LoginFilter;
import com.youngcha.ez.global.security.jwt.CustomLogoutFilter;
import com.youngcha.ez.member.domain.repository.MemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, CustomAuthenticationEntryPoint customAuthenticationEntryPoint,
        JwtUtil jwtUtil,
        RefreshTokenRepository refreshTokenRepository, MemberRepository memberRepository) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
        this.memberRepository = memberRepository;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
        throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173",
            "https://ec2-43-201-127-225.ap-northeast-2.compute.amazonaws.com:8080"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.addExposedHeader("Authorization");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // cors disable
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        // csrf disable
        http
            .csrf((auth) -> auth.disable());

        // Form 로그인 방식 disable
        http
            .formLogin((auth) -> auth.disable());

        // http basic 인증 방식 disable
        http
            .httpBasic((auth) -> auth.disable());

        http
            .logout((auth) -> auth.disable());

        // 경로별 인가 작업
        http
            .authorizeRequests(auth -> auth
                .requestMatchers("/", "/user/login", "/user/join").permitAll() // 인증 없이 접근 허용
                .anyRequest().authenticated() // 다른 모든 요청은 인증 필요
            )
            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint(customAuthenticationEntryPoint)
            );

        http
            .addFilterBefore(new JwtFilter(jwtUtil), LoginFilter.class);

        http
            .addFilterAt(
                new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil,
                    refreshTokenRepository, memberRepository),
                UsernamePasswordAuthenticationFilter.class);

        http
            .addFilterAt(new CustomLogoutFilter(jwtUtil, refreshTokenRepository),
                LogoutFilter.class);

        // 세션 설정 (jwt 방식에서는 session을 stateless로 설정)
        http
            .sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
