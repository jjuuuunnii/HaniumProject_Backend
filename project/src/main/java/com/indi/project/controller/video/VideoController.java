package com.indi.project.controller.video;
import com.indi.project.dto.user.req.FollowingDto;
import com.indi.project.dto.video.GetViewDto;
import com.indi.project.service.follow.FollowService;
import com.indi.project.success.SuccessCode;
import com.indi.project.success.SuccessObject;
import com.indi.project.dto.commet.req.CommentDeleteReqDto;
import com.indi.project.dto.commet.req.CommentReqDto;
import com.indi.project.dto.like.LikesDto;
import com.indi.project.dto.video.VideoGetDto;
import com.indi.project.dto.video.VideoListDto;
import com.indi.project.entity.Genre;
import com.indi.project.service.comment.CommentService;
import com.indi.project.service.like.LikeService;
import com.indi.project.service.video.VideoService;
import com.indi.project.success.likeAndSubscribe.LsSuccessObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/videos")
public class VideoController {

    private final VideoService videoService;
    private final CommentService commentService;
    private final LikeService likeService;
    private final FollowService followService;

    @GetMapping("/{genre}")
    public List<VideoListDto> getVideoList(@PathVariable String genre) {
        Genre genreEnum;
        try {
            genreEnum = Genre.fromString(genre);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid genre");
        }
        return videoService.getVideos(genreEnum);
    }

    @GetMapping("/user/{videoId}")
    public VideoGetDto getVideo(@PathVariable Long videoId) {
        return videoService.getVideo(videoId);
    }

    @PostMapping("/{videoId}/comments")
    public SuccessObject saveComments(@PathVariable Long videoId, @RequestBody CommentReqDto commentReqDto){  //비디오Id
        commentService.saveComments(videoId, commentReqDto);
        return new SuccessObject(SuccessCode.COMMENT_POSTED.isSuccess(), SuccessCode.COMMENT_POSTED.getCode());
    }

    @DeleteMapping("/{videoId}/comments")
    public SuccessObject deleteComments(@PathVariable Long videoId, @RequestBody CommentDeleteReqDto commentDeleteReqDto){
        commentService.deleteComments(videoId, commentDeleteReqDto);
        return new SuccessObject(SuccessCode.COMMENT_DELETED.isSuccess(), SuccessCode.COMMENT_DELETED.getCode());
    }

    @PutMapping("/{videoId}/likes")
    public LsSuccessObject saveLikes(@PathVariable Long videoId, @RequestBody LikesDto likesDto){
        boolean result = likeService.saveLikes(videoId, likesDto);
        return new LsSuccessObject(SuccessCode.LIKE_STATUS_POSTED.isSuccess(), result, SuccessCode.LIKE_STATUS_POSTED.getCode());
    }

    @PutMapping("/{videoId}/follow")
    public LsSuccessObject saveFollows(@PathVariable Long videoId, @RequestBody FollowingDto followingDto) {
        boolean result = followService.saveFollow(videoId, followingDto);
        return new LsSuccessObject(SuccessCode.FOLLOW_POSTED.isSuccess(), result, SuccessCode.FOLLOW_POSTED.getCode());
    }

    @PostMapping("/{videoId}/views")
    public SuccessObject increaseViews(@PathVariable Long videoId, @RequestBody GetViewDto getViewDto){
        videoService.increaseViews(videoId, getViewDto.getLoginId());
        return new SuccessObject(SuccessCode.VIEW_INCREASED_POSTED.isSuccess(), SuccessCode.VIEW_INCREASED_POSTED.getCode());
    }
}

