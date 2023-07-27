package com.indi.project.config;

import com.indi.project.repository.UserRepository;
import com.indi.project.security.CustomAccessDeniedHandler;
import com.indi.project.security.CustomAuthenticationEntryPoint;
import com.indi.project.service.filter.JwtAuthenticationFilter;
import com.indi.project.service.filter.JwtAuthorizationFilter;
import com.indi.project.service.json.JsonService;
import com.indi.project.service.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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
    private final JwtService jwtService;
    private final JsonService jsonService;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        JwtAuthorizationFilter jwtAuthorizationFilter = new JwtAuthorizationFilter(authenticationConfiguration.getAuthenticationManager(), jwtService, jsonService);
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationConfiguration.getAuthenticationManager(), jwtService, jsonService);

        http
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(corsFilter)
                .formLogin().disable()
                .httpBasic().disable()
                .authorizeRequests()
                .antMatchers("/auth/signup/**", "/", "/login").permitAll()
                //영상 목록 불러올때, 영상한개가져올때, 랭킹, 서치
                .anyRequest().authenticated()
                //좋아요, 구독, 마이페이지, 스튜디오, 댓글달기,
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .and()
                .addFilter(jwtAuthenticationFilter)
                .addFilter(jwtAuthorizationFilter);

        return http.build();
    }
}




