package com.indi.project.controller;

import com.indi.project.dto.user.req.UserJoinReqDto;
import com.indi.project.dto.user.res.UserJoinResDto;
import com.indi.project.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@ToString
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public UserJoinResDto joinUser(@RequestBody UserJoinReqDto userJoinReqDto) {
        log.info("userJoinReqDto={}", userJoinReqDto.toString());
        /*if(bindingResult.hasErrors()){
            throw new NullPointerException("값을 입력해주세요");
        }*/
        return userService.joinUser(userJoinReqDto);
    }

}
