package org.zerock.apiserver.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.zerock.apiserver.security.handler.APILoginFailHandler;
import org.zerock.apiserver.security.handler.APILoginSuccessHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@Log4j2
@RequiredArgsConstructor
public class CustomSecurityConfig {
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception:
//    이 메소드는 SecurityFilterChain 타입의 빈을 생성하고 반환합니다.
//    HttpSecurity 객체는 웹 보안 설정을 구성하는 데 사용됩니다.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("----------------security config-------------------");

        //header 설정
        http.cors(httpSecurityCorsConfigurer -> {
            httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource());
        });

        //세션 못만들도록 만드는것
        http.sessionManagement(httpSecuritySessionManagementConfigurer -> {
            httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.NEVER);
        });

        //csrf설정 -> request 위조 방지
        http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());

        // 폼 로그인을 활성화하고, 로그인 페이지를 /api/member/login으로 설정합니다. 이 설정은 사용자가 로그인할 때 사용할 페이지를 지정합니다.
        http.formLogin(config -> {
            //로그인 페이지
            config.loginPage("/api/member/login");
            //로그인 성공시 핸들러 사용 -> 성공시 핸들러 사용
            config.successHandler(new APILoginSuccessHandler());
            //json data 필요
//            [Principal=org.zerock.apiserver.dto.MemberDTO [Username=user9@aaa.com, Password=[PROTECTED], Enabled=true, AccountNonExpired=true, CredentialsNonExpired=true, AccountNonLocked=true, Granted Authorities=[ROLE_ADMIN, ROLE_MANAGER, ROLE_USER]], Credentials=[PROTECTED], Authenticated=true, Details=WebAuthenticationDetails [RemoteIpAddress=0:0:0:0:0:0:0:1, SessionId=null], Granted Authorities=[ROLE_ADMIN, ROLE_MANAGER, ROLE_USER]]
            //여기서 MemberDto를 json문자열로 바꾼다 -> APILoginSuccessHandler 여기서 동작
            config.failureHandler(new APILoginFailHandler());
        });

        //return http.build();: 구성된 HttpSecurity 설정을 바탕으로 SecurityFilterChain 객체를 빌드하고 반환합니다.
        // 이 객체는 Spring Security 필터 체인을 구성하고, 요청을 처리하는 데 사용됩니다.
        return http.build();
    }

    //password
    //사용자 계정 암호
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //ajax 사용시 cross origin 문제 해결
    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOriginPatterns(Arrays.asList("*"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }
}
