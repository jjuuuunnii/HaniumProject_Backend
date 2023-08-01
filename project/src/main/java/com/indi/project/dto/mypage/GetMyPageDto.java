package com.indi.project.dto.mypage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class GetMyPageDto {

    private String name;
    private String loginId;
    private String nickName;

    private List<FollowingListDto> following;
    private List<LikeListDto> like;

    private String profileImageUrl;

    public GetMyPageDto(){}
}
