package com.indi.project.dto.user.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FollowingDto {

    private String loginId;

    private String nickName;

    private String profileImageUrl;

}
