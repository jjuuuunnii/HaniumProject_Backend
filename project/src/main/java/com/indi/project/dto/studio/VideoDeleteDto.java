package com.indi.project.dto.studio;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class VideoDeleteDto {
    private List<Long> videoIds;

    public VideoDeleteDto(){}
}
