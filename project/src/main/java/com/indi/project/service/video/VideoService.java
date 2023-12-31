package com.indi.project.service.video;

import com.indi.project.dto.user.req.FollowingDto;
import com.indi.project.dto.video.CommentDto;
import com.indi.project.dto.video.VideoGetDto;
import com.indi.project.dto.video.VideoListDto;
import com.indi.project.entity.*;
import com.indi.project.exception.CustomException;
import com.indi.project.exception.ErrorCode;
import com.indi.project.repository.*;
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
    private final ViewRepository viewRepository;
    private final UserRepository userRepository;

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
    public void increaseViews(Long videoId, String loginId) {
        Video video = videoRepository.findById(videoId).orElseThrow(() -> new CustomException(ErrorCode.VIDEO_NOT_FOUND));
        User user = userRepository.findByLoginId(loginId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        View view = viewRepository.findViewByUserAndVideo(user, video).orElseGet(() -> {
            View newView = new View();
            newView.setUser(user);
            newView.setVideo(video);
            user.getViews().add(newView);
            video.getViews().add(newView);
            viewRepository.save(newView);
            return newView;
        });
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
                    videoListDto.setViews(video.getViews().size());
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
                .views(video.getViews().size())
                .videoUrl(video.getVideoPath())
                .comments(video.getComments().stream()
                        .map(comment -> new CommentDto(
                                comment.getUser().getLoginId(),
                                comment.getContent(),
                                comment.getCreateAt().toString(),
                                comment.getUser().getProfileImageUrl())
                        )
                        .collect(Collectors.toList()))
                .profileImageUrl(video.getUser().getProfileImageUrl())
                .time(LocalDateTime.now().toString())
                .build();
    }
}
