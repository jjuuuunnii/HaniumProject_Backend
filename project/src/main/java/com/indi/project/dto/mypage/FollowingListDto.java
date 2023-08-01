package com.indi.project.dto.mypage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class FollowingListDto {

    private String loginId;
    private String nickName;
    private String profileImageUrl;

    public FollowingListDto(){};
}
