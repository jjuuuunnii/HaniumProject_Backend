package com.indi.project.dto.video;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CommentDto {
    private String loginId;
    private String content;
    private String time;
    private String profileImageUrl;
}
