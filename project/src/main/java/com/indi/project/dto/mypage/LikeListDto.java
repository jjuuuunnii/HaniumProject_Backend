package com.indi.project.dto.mypage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class LikeListDto {

    private String thumbnail;
    private String title;
    private String nickName;

    public LikeListDto(){}
}
