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
    public boolean saveLikes(Long videoId, LikesDto likesDto) {

        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new CustomException(ErrorCode.VIDEO_NOT_FOUND));
        User user = userRepository.findByLoginId(likesDto.getLoginId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Optional<Like> existingLikeOpt = likeRepository.findByUserIdAndVideoId(user.getId(), video.getId());

        if(existingLikeOpt.isPresent()) {
            // 이미 존재하는 좋아요 - 좋아요 상태를 변경
            Like existingLike = existingLikeOpt.get();
            existingLike.setLikeStatus(!existingLike.isLikeStatus());
            return existingLike.isLikeStatus();
        } else {
            // 새로운 좋아요 - 좋아요 객체를 생성하고 저장
            Like like = Like.builder()
                    .user(user)
                    .video(video)
                    .likeStatus(true)
                    .build();

            video.getLikes().add(like);
            user.getLikes().add(like);  //양방향 연관관계 설정

            likeRepository.save(like);
            return true;
        }
    }

}
