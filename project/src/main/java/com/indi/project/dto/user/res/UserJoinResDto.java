package com.indi.project.dto.user.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class UserJoinResDto {
    public boolean success;
    public String code;

    public UserJoinResDto(){}
}
