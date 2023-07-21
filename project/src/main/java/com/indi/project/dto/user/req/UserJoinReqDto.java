package com.indi.project.dto.user.req;

import com.indi.project.entity.User;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserJoinReqDto {

    @NotNull
    private String name;
    @NotNull
    private String nickName;
    @NotNull
    private String loginId;
    @NotNull
    private String password;

    public UserJoinReqDto(){}

    public User toEntity(String password){
        return User.builder()
                .name(name)
                .nickName(nickName)
                .loginId(loginId)
                .password(password)
                .createAt(LocalDateTime.now())
                .build();
    }
}


