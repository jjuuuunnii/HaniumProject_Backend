package com.indi.project.dto.user.req;

import com.indi.project.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class UserJoinReqDto {
    private String name;
    private String nickName;
    private String loginId;
    private String password;

    @Builder
    public UserJoinReqDto(String name, String nickName, String loginId, String password) {
        this.name = name;
        this.nickName = nickName;
        this.loginId = loginId;
        this.password = password;
    }

    public User toEntity(String password){
        return User.builder()
                .name(name)
                .nickName(nickName)
                .loginId(loginId)
                .password(password)
                .followerCnt(0)
                .createAt(LocalDateTime.now())
                .build();
    }
}


