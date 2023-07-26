package com.indi.project.controller.token;

import com.indi.project.dto.Token.TokenDto;
import com.indi.project.dto.user.req.RefreshTokenMessage;
import com.indi.project.dto.user.req.UserLoginReqDto;
import com.indi.project.dto.user.res.UserLoginResDto;
import com.indi.project.service.jwt.JwtProperties;
import com.indi.project.service.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/refresh")
public class TokenController implements RefreshTokenMessage {

    private final JwtService jwtService;


    //엑세스 토큰 재발급하기
    @PostMapping("/accessToken")
    public ResponseEntity<UserLoginResDto> refreshAccessToken(
            @RequestBody TokenDto tokenDto,
            HttpServletResponse response
            ){

        jwtService.setNewAccessToken(tokenDto,response);
        return new ResponseEntity<>(new UserLoginResDto(CODE, MESSAGE, tokenDto.getLoginId()), HttpStatus.OK);
    }


}
