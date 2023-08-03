package com.indi.project.dto.studio;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Access;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class VideoDeleteDtoV2 {
    private List<Long> videoIds;

    public VideoDeleteDtoV2(){}
}
