package com.indi.project.security;

import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Log4j2
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest req, HttpServletResponse res, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        log.error("Access Denied Handler");
        log.error("Redirect....");

        //response.sendRedirect("/accessError");
        setResponse(res, accessDeniedException.getMessage());
    }

    private void setResponse(HttpServletResponse res, String message) throws IOException {
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        res.setContentType("application/json;charset=UTF-8");

        JSONObject jObj = new JSONObject();
        jObj.put("success", false);
        jObj.put("code", -1);
        jObj.put("message", message);

        res.getWriter().write(jObj.toString());
    }

    @GetMapping(value = "accessDenied")
    public void accessDenied() {
        log.error("Access Denied Handler");
        log.error("Redirect....");

        throw new AccessDeniedException("Access Denied");
    }
}
