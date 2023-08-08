package com.indi.project.service.studio;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.indi.project.dto.studio.UserVideoListDto;
import com.indi.project.dto.studio.VideoDeleteDto;
import com.indi.project.dto.studio.VideoJoinDto;
import com.indi.project.entity.Genre;
import com.indi.project.entity.User;
import com.indi.project.entity.Video;
import com.indi.project.exception.CustomException;
import com.indi.project.exception.ErrorCode;
import com.indi.project.repository.UserRepository;
import com.indi.project.repository.VideoRepository;
import com.indi.project.service.file.FileService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class StudioService {
    private final VideoRepository videoRepository;
    private final UserRepository userRepository;
    private final FileService fileService;

    @Value("${cloud.aws.s3.thumbNail-folder}")
    private String thumbnailDir;

    @Value("${cloud.aws.s3.video-folder}")
    private String videoFileDir;



    @Transactional
    public void joinUserVideo(VideoJoinDto videoJoinDto, String loginId) {
        User user = userRepository.findByLoginId(loginId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        try {
            String videoFilePath = fileService.saveFile(videoJoinDto.getVideoFile(), videoFileDir);
            String thumbNailFilePath = fileService.saveFile(videoJoinDto.getThumbNail(), thumbnailDir);

            Video video = getVideo(videoJoinDto, user, videoFilePath, thumbNailFilePath);
            videoRepository.save(video);

        } catch (IOException e) {
            throw new CustomException(ErrorCode.STORE_FILES_FAILS);
        }
    }

    public List<UserVideoListDto> getUserVideoList(String loginId) {
        User user = userRepository.findByLoginId(loginId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return user.getVideos().stream()
                .map(video -> UserVideoListDto.builder()
                        .thumbNailPath(video.getThumbNailPath())
                        .videoTitle(video.getTitle())
                        .videoFilePath(video.getVideoPath())
                        .likes(video.totalLikesCnt())
                        .views(video.getViews().size())
                        .nickName(user.getNickName())
                        .uploadDateTime(video.getCreatedAt().toString())
                        .genre(video.getGenre().toString())
                        .build())
                .collect(Collectors.toList());
    }

    public void deleteUserVideo(String loginId, VideoDeleteDto videoDeleteDto) {
        User user = userRepository.findByLoginId(loginId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        for (Long videoId : videoDeleteDto.getVideoIds()) {
            fileService.deleteVideo(user, videoId);
        }
    }

    private Video getVideo(VideoJoinDto videoJoinDto, User user, String videoFilePath, String thumbNailFilePath) {
        return Video.builder()
                .user(user)
                .genre(Genre.fromString(videoJoinDto.getGenre()))
                .videoPath(videoFilePath)
                .thumbNailPath(thumbNailFilePath)
                .title(videoJoinDto.getTitle())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
