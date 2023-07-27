package com.indi.project.service.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.indi.project.dto.user.req.UserLoginReqDto;
import com.indi.project.entity.User;
import com.indi.project.security.userService.PrincipalDetails;
import com.indi.project.service.json.JsonService;
import com.indi.project.service.jwt.JwtProperties;
import com.indi.project.service.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        ObjectMapper om = new ObjectMapper();

        try {
            log.info("=========================로그인 시도============================");
            UserLoginReqDto login = om.readValue(request.getInputStream(), UserLoginReqDto.class);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(login.getLoginId(), login.getPassword());

            Authentication authenticate = authenticationManager.authenticate(authenticationToken);
            log.info("=========================인증 성공============================");
            return authenticate;
        }
        catch (IOException e) {
            log.debug("error = {} ", "값이 잘못 들어왔습니다");
        }
        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        PrincipalDetails principal = (PrincipalDetails) authResult.getPrincipal();
        User user = principal.getUser();
        String accessToken = jwtService.createAccessToken(user.getLoginId(), user.getName());
        String refreshToken = jwtService.createRefreshToken();

        response.addHeader(JwtProperties.ACCESS_HEADER_PREFIX, accessToken);
        response.addHeader(JwtProperties.REFRESH_HEADER_PREFIX, refreshToken);

        jwtService.setRefreshToken(user.getLoginId(), refreshToken);
        log.info("이름: = {}, 닉네임 = {} 로그인 성공", user.getLoginId(), user.getName());
        setSuccessResponse(response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.error("=========================로그인 실패============================");
        log.info("이유 = {}", failed.getCause());
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("application/json;charset=UTF-8");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", false);
        jsonObject.put("code", "LOGOUT FAILED");
        response.getWriter().write(jsonObject.toString());
    }

    public void setSuccessResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", true);
        jsonObject.put("code", "LOGIN SUCCESS");
        response.getWriter().write(jsonObject.toString());
    }
}*/



@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final JsonService jsonService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            log.info("=========================로그인 시도============================");
            UserLoginReqDto login = new ObjectMapper().readValue(request.getInputStream(), UserLoginReqDto.class);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(login.getLoginId(), login.getPassword()));
        } catch (IOException e) {
            log.debug("error = {} ", "값이 잘못 들어왔습니다");
            throw new AuthenticationServiceException("값이 잘못 들어왔습니다", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        PrincipalDetails principal = (PrincipalDetails) authResult.getPrincipal();
        User user = principal.getUser();
        String accessToken = jwtService.createAccessToken(user.getLoginId(), user.getName());
        String refreshToken = jwtService.createRefreshToken();

        response.addHeader(JwtProperties.ACCESS_HEADER_PREFIX, accessToken);
        response.addHeader(JwtProperties.REFRESH_HEADER_PREFIX, refreshToken);

        jwtService.setRefreshToken(user.getLoginId(), refreshToken);
        log.info("이름: = {}, 닉네임 = {} 로그인 성공", user.getLoginId(), user.getName());
        sendResponse(response, true, "LOGIN SUCCESS", HttpServletResponse.SC_OK);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        log.error("=========================로그인 실패============================");
        log.info("이유 = {}", failed.getCause());
        sendResponse(response, false, "LOGIN FAILED", HttpServletResponse.SC_UNAUTHORIZED);
    }

    private void sendResponse(HttpServletResponse response, boolean success, String code, int status) throws IOException {
        jsonService.responseToJson(response, status, success, code);
    }
}

