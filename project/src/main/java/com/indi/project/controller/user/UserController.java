package com.indi.project.controller.user;

import com.indi.project.dto.mypage.GetMyPageDto;
import com.indi.project.dto.user.req.UserEditInfoDto;
import com.indi.project.dto.user.req.UserLeaveDto;
import com.indi.project.service.video.VideoService;
import com.indi.project.success.Result;
import com.indi.project.dto.user.req.UserJoinReqDto;
import com.indi.project.dto.user.res.UserJoinResDto;
import com.indi.project.service.json.JsonService;
import com.indi.project.service.jwt.JwtService;
import com.indi.project.service.user.UserService;
import com.indi.project.success.SuccessCode;
import com.indi.project.success.SuccessObject;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    private final VideoService videoService;

    @PostMapping("/auth/signup")
    public UserJoinResDto joinUser(@Validated @RequestBody UserJoinReqDto userJoinReqDto) {
        log.info("userJoinReqDto={}", userJoinReqDto.toString());

        return userService.joinUser(userJoinReqDto);
    }

    @PostMapping("/auth/logout")
    public SuccessObject logoutUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        jwtService.logout(request);
        return new SuccessObject(SuccessCode.LOGOUT_SUCCESS.isSuccess(), SuccessCode.LOGOUT_SUCCESS.getCode());
    }

    @GetMapping("/mypage/{loginId}")
    public GetMyPageDto getMypageInfo(@PathVariable String loginId) {
        return userService.getMyPageInfo(loginId);
    }

    @PostMapping("/auth/leave")
    public SuccessObject leave(@RequestBody UserLeaveDto userLeaveDto){
        userService.leaveUser(userLeaveDto);
        return new SuccessObject(SuccessCode.USER_DELETED.isSuccess(), SuccessCode.USER_DELETED.getCode());
    }

    @PutMapping("/auth/edit/{loginId}")
    public SuccessObject editUserInfo(@PathVariable String loginId,
                                              @RequestPart String name,
                                              @RequestPart String nickName,
                                              @RequestPart MultipartFile profileImage
                                              ) {
        UserEditInfoDto userEditInfoDto = UserEditInfoDto.builder()
                .name(name)
                .nickName(nickName)
                .profileImage(profileImage)
                .build();

        userService.editUserInfo(loginId,userEditInfoDto);
        return new SuccessObject(SuccessCode.USER_EDITED.isSuccess(), SuccessCode.USER_EDITED.getCode());
    }
}




