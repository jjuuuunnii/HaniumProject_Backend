package com.indi.project.dto.studio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserVideoListDto {

    private String thumbNailPath;
    private String videoTitle;
    private String videoFilePath;
    private int likes;
    private int views;
    private String nickName;
    private String uploadDateTime;
    private String genre;

    public UserVideoListDto(){}
}
