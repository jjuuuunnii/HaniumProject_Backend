package com.indi.project.security;

import com.indi.project.service.jwt.JwtProperties;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        log.error("비로그인 사용자 요청");
        setResponse(response);
    }
    private void setResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        JSONObject jsonObject = new JSONObject();
        JSONObject result = new JSONObject();
        jsonObject.put("success", "false");
        jsonObject.put("code", "LOGIN FAILED");
        result.put("data", jsonObject);
        response.getWriter().write(result.toString());
    }
}