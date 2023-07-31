package com.indi.project.dto.user.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserMypageInfoResDto {

    private String name;

    private String loginId;

    private String nickName;

//    private List<Follow>

    public UserMypageInfoResDto() {}
}
