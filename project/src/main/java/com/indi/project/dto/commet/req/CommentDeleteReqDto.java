package com.indi.project.dto.commet.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CommentDeleteReqDto {
    private String loginId;
    private LocalDateTime time;
}
