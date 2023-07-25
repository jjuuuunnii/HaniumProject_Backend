package com.indi.project.controller.user;

import com.indi.project.dto.user.req.UserJoinReqDto;
import com.indi.project.dto.user.res.UserJoinResDto;
import com.indi.project.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
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
    public UserJoinResDto joinUser(@Validated @RequestBody UserJoinReqDto userJoinReqDto) {
        log.info("userJoinReqDto={}", userJoinReqDto.toString());

        return userService.joinUser(userJoinReqDto);
    }



}
