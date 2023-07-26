package com.indi.project.service.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.indi.project.dto.user.req.UserLoginReqDto;
import com.indi.project.entity.User;
import com.indi.project.security.userService.PrincipalDetails;
import com.indi.project.service.jwt.JwtProperties;
import com.indi.project.service.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        ObjectMapper om = new ObjectMapper();

        try {
            log.info("login Attempting");
            UserLoginReqDto login = om.readValue(request.getInputStream(), UserLoginReqDto.class);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(login.getLoginId(), login.getPassword());

            Authentication authenticate = authenticationManager.authenticate(authenticationToken);
            log.info("=====================================================");
            log.info("로그인 성공");
            return authenticate;
        }
        catch (IOException e) {
            log.debug("error = {} ", e);
            throw new RuntimeException(e);
        }
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
        log.info(user.getLoginId().toString() + "====login success===");
        setSuccessResponse(response, user.getLoginId());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.error("Fail Login");
        log.info("error={}", failed.getCause());
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("application/json;charset=UTF-8");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", false);
        jsonObject.put("code", 400);
        jsonObject.put("message", "login failed");

        response.getWriter().write(jsonObject.toString());
    }

    public void setSuccessResponse(HttpServletResponse response, String loginId) throws IOException {

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", true);
        jsonObject.put("code", 200);
        jsonObject.put("message", "login Success");
        response.getWriter().write(jsonObject.toString());
    }
}