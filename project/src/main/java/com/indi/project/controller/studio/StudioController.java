package com.indi.project.controller.studio;

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

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/studio")
public class StudioController {

    private final StudioService studioService;

    @GetMapping("/{loginId}/upload")
    public Result<List<UserVideoListDto>> getUserVideoList(@PathVariable String loginId){
        List<UserVideoListDto> userVideoList = studioService.getUserVideoList(loginId);
        return new Result<>(userVideoList);
    }

    @PostMapping("/{loginId}/upload")
    public Result<SuccessObject> joinUserVideo(@RequestPart VideoJoinDto videoJoinDto, @PathVariable String loginId) {
        studioService.joinUserVideo(videoJoinDto, loginId);
        return new Result<>(new SuccessObject(SuccessCode.VIDEO_POSTED.isSuccess(), SuccessCode.VIDEO_POSTED.getCode()));
    }

    @PostMapping("/{videoId}/delete")
    public Result<SuccessObject> deleteVideo(@RequestBody String loginId, @PathVariable Long videoId) {
        studioService.deleteUserVideo(loginId, videoId);
        return new Result<>(new SuccessObject(SuccessCode.VIDEO_DELETED.isSuccess(), SuccessCode.VIDEO_DELETED.getCode()));
    }



}