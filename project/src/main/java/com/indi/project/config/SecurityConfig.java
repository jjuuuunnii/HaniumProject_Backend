package com.indi.project.config;

import com.indi.project.entity.User;
import com.indi.project.security.CustomAccessDeniedHandler;
import com.indi.project.security.CustomAuthenticationEntryPoint;
import com.indi.project.service.filter.JWTAuthenticationFilter;
import com.indi.project.service.filter.JWtAuthorizationFilter;
import com.indi.project.service.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CorsFilter corsFilter;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtService jwtService;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        AuthenticationManager authenticationManager = authenticationConfiguration.getAuthenticationManager();

        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .addFilter(corsFilter)
                .addFilter(new JWTAuthenticationFilter(authenticationManager, jwtService))
                .addFilter(new JWtAuthorizationFilter(authenticationManager, jwtService))
                .authorizeRequests()
                .antMatchers("/login/**").permitAll() // 로그인 경로는 누구나 접근 가능
                .antMatchers("/signup/**").permitAll()
                .antMatchers("/").permitAll()
                .anyRequest().authenticated()// 나머지 요청은 모두 인증된 사용자만 접근 가능
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint); // 인증 실패 시 401 Unauthorized 반환

        return http.build();
    }

}



