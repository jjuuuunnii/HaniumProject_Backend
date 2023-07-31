package com.indi.project.service.like;

import com.indi.project.dto.like.LikesDto;
import com.indi.project.entity.Like;
import com.indi.project.entity.User;
import com.indi.project.entity.Video;
import com.indi.project.exception.CustomException;
import com.indi.project.exception.ErrorCode;
import com.indi.project.repository.LikeRepository;
import com.indi.project.repository.UserRepository;
import com.indi.project.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class LikeService {

    private final VideoRepository videoRepository;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;

    @Transactional
    public void saveLikes(Long videoId, LikesDto likesDto) {
        Like like = Like.builder()
                .likeStatus(likesDto.isLikeStatus())
                .build();

        Video video = videoRepository.findById(videoId).orElseThrow(() -> new CustomException(ErrorCode.VIDEO_NOT_FOUND));
        User user = userRepository.findByLoginId(likesDto.getLoginId()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        like.setUser(user);
        like.setVideo(video);

        video.getLikes().add(like);
        user.getLikes().add(like);
        likeRepository.save(like);
    }
}
