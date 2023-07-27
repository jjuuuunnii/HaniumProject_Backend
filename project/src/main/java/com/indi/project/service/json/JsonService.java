package com.indi.project.service.json;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class JsonService {

    public void responseToJson(HttpServletResponse response,
                               int status,
                               boolean success,
                               String code) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        JSONObject jsonObject = new JSONObject();
        JSONObject result = new JSONObject();

        jsonObject.put("success", success);
        jsonObject.put("code", code);
        result.put("data", jsonObject);
        response.getWriter().write(result.toString());
    }
}
