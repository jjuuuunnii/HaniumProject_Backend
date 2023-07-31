package com.indi.project.controller.user;

import com.indi.project.Result;
import com.indi.project.dto.user.req.UserJoinReqDto;
import com.indi.project.dto.user.res.UserJoinResDto;
import com.indi.project.dto.user.res.UserLogoutResDto;
import com.indi.project.service.json.JsonService;
import com.indi.project.service.jwt.JwtService;
import com.indi.project.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
@ToString
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;
    private final JsonService jsonService;

    @PostMapping("/auth/signup")
    public Result<UserJoinResDto> joinUser(@Validated @RequestBody UserJoinReqDto userJoinReqDto) {
        log.info("userJoinReqDto={}", userJoinReqDto.toString());

        return new Result<>(userService.joinUser(userJoinReqDto));
    }
    @PutMapping("/auth/logout")
    public void logoutUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        jwtService.logout(request);
        jsonService.responseToJson(response, HttpServletResponse.SC_OK,true,"LOGOUT SUCCESS");
    }



}
