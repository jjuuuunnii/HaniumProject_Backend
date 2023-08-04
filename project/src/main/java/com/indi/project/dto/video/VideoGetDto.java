package com.indi.project.dto.video;

import com.indi.project.entity.Comment;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class VideoGetDto {

    private Long videoId;
    private String title;
    private int likes;
    private String loginId;
    private int views;
    private List<CommentDto> comments;
    private String videoUrl;
    private String profileImageUrl;
    private String time;

    public VideoGetDto(){

    }
}
