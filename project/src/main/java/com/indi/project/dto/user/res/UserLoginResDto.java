package com.indi.project.dto.user.res;

import lombok.*;

@Getter
@ToString
@Builder
@AllArgsConstructor
public class UserLoginResDto {


    private String code;
    private String message;
    private String loginId;

}
