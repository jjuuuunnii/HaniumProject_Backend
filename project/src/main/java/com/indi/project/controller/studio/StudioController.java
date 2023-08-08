package com.indi.project.controller.studio;


import com.indi.project.dto.studio.VideoDeleteDto;
import com.indi.project.success.SuccessCode;
import com.indi.project.success.SuccessObject;
import com.indi.project.dto.studio.UserVideoListDto;
import com.indi.project.dto.studio.VideoJoinDto;
import com.indi.project.service.studio.StudioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
                                               @RequestPart MultipartFile thumbnail,
                                               @RequestParam String title,
                                               @RequestParam String genre,
                                               @PathVariable String loginId) {
        VideoJoinDto videoJoinDto = VideoJoinDto.builder()
                .videoFile(videoFile)
                .thumbNail(thumbnail)
                .title(title)
                .genre(genre)
                .build();

        studioService.joinUserVideo(videoJoinDto, loginId);
        return new SuccessObject(SuccessCode.VIDEO_POSTED.isSuccess(), SuccessCode.VIDEO_POSTED.getCode());
    }


    @DeleteMapping("/{loginId}/delete")
    public SuccessObject deleteVideo(@RequestBody VideoDeleteDto videoDeleteDto, @PathVariable String loginId) {
        studioService.deleteUserVideo(loginId, videoDeleteDto);
        return new SuccessObject(SuccessCode.VIDEO_DELETED.isSuccess(), SuccessCode.VIDEO_DELETED.getCode());
    }
}
