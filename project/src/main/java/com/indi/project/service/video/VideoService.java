package com.indi.project.service.video;

import com.indi.project.dto.user.req.FollowingDto;
import com.indi.project.dto.video.VideoGetDto;
import com.indi.project.dto.video.VideoListDto;
import com.indi.project.entity.*;
import com.indi.project.exception.CustomException;
import com.indi.project.exception.ErrorCode;
import com.indi.project.repository.CommentRepository;
import com.indi.project.repository.FollowRepository;
import com.indi.project.repository.UserRepository;
import com.indi.project.repository.VideoRepository;
import com.indi.project.service.json.JsonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class VideoService {
    private final VideoRepository videoRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    public VideoGetDto getVideo(Long id) {
        return videoRepository.findById(id)
                .map(video -> getVideoGetDto(id, video))
                .orElseThrow(() -> new CustomException(ErrorCode.GET_VIDEO_INFO_FAIL));
    }

    public List<VideoListDto> getVideos(Genre genre) {
        List<Video> byGenre = videoRepository.findByGenre(genre);
        if(byGenre.isEmpty()){
            throw new CustomException(ErrorCode.GET_VIDEOS_INFO_FAIL);
        }
        return getVideoListDtos(byGenre);
    }

    @Transactional
    public void increaseViews(Long videoId) {
        Video video = videoRepository.findById(videoId).orElseThrow(() -> new CustomException(ErrorCode.VIDEO_NOT_FOUND));
        video.increaseViewCnt();
    }



    private static List<VideoListDto> getVideoListDtos(List<Video> byGenre) {
        return byGenre.stream()
                .map(video -> {
                    VideoListDto videoListDto = new VideoListDto();

                    videoListDto.setVideoId(video.getId());
                    videoListDto.setTitle(video.getTitle());
                    videoListDto.setLikes(video.totalLikesCnt());
                    videoListDto.setThumbnail(video.getThumbNailPath());
                    videoListDto.setLoginId(video.getUser().getLoginId());
                    videoListDto.setViews(video.getViews());
                    videoListDto.setProfileImageUrl(video.getUser().getProfileImageUrl());

                    return videoListDto;
                })
                .collect(Collectors.toList());
    }

    private static VideoGetDto getVideoGetDto(Long id, Video video) {
        return VideoGetDto.builder()
                .videoId(id)
                .title(video.getTitle())
                .likes(video.totalLikesCnt())
                .loginId(video.getUser().getLoginId())
                .views(video.getViews())
                .comments(video.getComments())
                .profileImageUrl(video.getUser().getProfileImageUrl())
                .time(LocalDateTime.now())
                .build();
    }
}
