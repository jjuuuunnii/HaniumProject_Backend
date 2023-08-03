package com.indi.project.controller.studio;

import com.indi.project.dto.studio.VideoDeleteDto;
import com.indi.project.dto.studio.VideoDeleteDtoV2;
import com.indi.project.success.Result;
import com.indi.project.success.SuccessCode;
import com.indi.project.success.SuccessObject;
import com.indi.project.dto.studio.UserVideoListDto;
import com.indi.project.dto.studio.VideoJoinDto;
import com.indi.project.service.studio.StudioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/studio")
public class StudioController {

    private final StudioService studioService;

    @GetMapping("/{loginId}/upload")
    public List<UserVideoListDto> getUserVideoList(@PathVariable String loginId){
        List<UserVideoListDto> userVideoList = studioService.getUserVideoList(loginId);
        return userVideoList;
    }

    @PostMapping("/{loginId}/upload")
    public SuccessObject joinUserVideo(@RequestPart MultipartFile videoFile,
                                               @RequestPart MultipartFile thumbNail,
                                               @RequestPart String title,
                                               @RequestPart String genre,
                                               @PathVariable String loginId) {
        VideoJoinDto videoJoinDto = VideoJoinDto.builder()
                .videoFile(videoFile)
                .thumbNail(thumbNail)
                .title(title)
                .genre(genre)
                .build();

        studioService.joinUserVideo(videoJoinDto, loginId);
        return new SuccessObject(SuccessCode.VIDEO_POSTED.isSuccess(), SuccessCode.VIDEO_POSTED.getCode());
    }

/*    @DeleteMapping("/{videoId}/delete")
    public Result<SuccessObject> deleteVideo(@RequestBody VideoDeleteDto videoDeleteDto, @PathVariable Long videoId) {

        studioService.deleteUserVideo(videoDeleteDto.getLoginId(), videoId);
        return new Result<>(new SuccessObject(SuccessCode.VIDEO_DELETED.isSuccess(), SuccessCode.VIDEO_DELETED.getCode()));
    }*/

    @DeleteMapping("/{loginId}/delete")
    public SuccessObject deleteVideoV2(@RequestBody VideoDeleteDtoV2 videoDeleteDtoV2, @PathVariable String loginId) {
        studioService.deleteUserVideoV2(loginId, videoDeleteDtoV2);
        return new SuccessObject(SuccessCode.VIDEO_DELETED.isSuccess(), SuccessCode.VIDEO_DELETED.getCode());
    }
}
