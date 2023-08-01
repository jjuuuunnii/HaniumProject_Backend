package com.indi.project.dto.user.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FollowingDto {
    private String loginId;
    private boolean followStatus;
}
