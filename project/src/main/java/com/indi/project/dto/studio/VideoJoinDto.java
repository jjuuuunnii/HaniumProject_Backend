package com.indi.project.dto.studio;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
public class VideoJoinDto {

    private MultipartFile videoFile;
    private MultipartFile thumbNail;
    private String title;
    private String genre;

    public VideoJoinDto(){}
}
