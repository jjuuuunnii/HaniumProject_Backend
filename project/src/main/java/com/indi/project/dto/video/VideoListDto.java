package com.indi.project.dto.video;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class VideoListDto {
    private Long videoId;
    private String thumbnail;
    private String title;
    private int likes;
    private String loginId;
    private int views;
    private String profileImageUrl;

    public VideoListDto(){}

}
