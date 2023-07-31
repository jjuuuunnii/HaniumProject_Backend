package com.indi.project.dto.commet.req;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CommentReqDto {

    private String loginId;
    private String content;
    private String profileImageName;

    public CommentReqDto(){};
}
