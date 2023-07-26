package com.indi.project.dto.user;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class UserJwtDto {
    private String name;
    private String loginId;
    private String password;

}
