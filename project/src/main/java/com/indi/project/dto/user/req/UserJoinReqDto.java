package com.indi.project.dto.user.req;
import com.indi.project.entity.User;
import lombok.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
public class UserJoinReqDto {

    @NotBlank(message = "{NOT_BLANK_NAME}")
    private String name;

    @NotBlank(message = "{NOT_BLANK_NICKNAME}")
    private String nickName;

    @NotBlank(message = "{NOT_BLANK_LOGINID}")
    private String loginId;

    @NotBlank(message = "{NOT_BLANK_PASSWORD}")
    private String password;

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


