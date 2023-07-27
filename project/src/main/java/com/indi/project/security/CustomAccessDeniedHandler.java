package com.indi.project.security;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


//인디에서는 필요없다고 생각
@Component
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.info("AccessDenied");
        response.sendRedirect("/accessDenied");

    }
    @GetMapping(value = "/accessDenied")
    public void accessDenied(HttpServletResponse response) {
        throw new AccessDeniedException("accessDenied");
    }

    private void setResponse(HttpServletResponse res, String message) throws IOException {
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        res.setContentType("application/json;charset=UTF-8");

        JSONObject jObj = new JSONObject();
        jObj.put("success", false);
        jObj.put("code", 401);
        jObj.put("message", message);

        res.getWriter().write(jObj.toString());
    }


}
