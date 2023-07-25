package com.indi.project.dto.user.req;

import lombok.*;

import javax.validation.constraints.NotBlank;


@Getter
@Setter
@ToString
@AllArgsConstructor
public class UserLoginReqDto {

    @NotBlank(message = "{NOT_BLANK_LOGINID}")
    private String loginId;

    @NotBlank(message = "{NOT_BLANK_PASSWORD}")
    private String password;

    public UserLoginReqDto(){}

}
