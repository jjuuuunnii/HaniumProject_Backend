package com.indi.project.service.follow;

import com.indi.project.dto.user.req.FollowingDto;
import com.indi.project.entity.Follow;
import com.indi.project.entity.User;
import com.indi.project.entity.Video;
import com.indi.project.exception.CustomException;
import com.indi.project.exception.ErrorCode;
import com.indi.project.repository.FollowRepository;
import com.indi.project.repository.UserRepository;
import com.indi.project.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class FollowService {

    private final FollowRepository followRepository;
    private final VideoRepository videoRepository;
    private final UserRepository userRepository;

    public boolean saveFollow(Long videoId, FollowingDto followingDto) {
        Video video = videoRepository.findById(videoId).orElseThrow(() -> new CustomException(ErrorCode.VIDEO_NOT_FOUND));
        User followedUser = video.getUser();
        User followingUser = userRepository.findByLoginId(followingDto.getLoginId()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // Prevent self-follow
        if (followedUser.equals(followingUser)) {
            throw new CustomException(ErrorCode.CANNOT_FOLLOW_SELF);
        }

        Follow existingFollow;
        try {
            existingFollow = followRepository.findFollowRelation(followingUser, followedUser);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.DATABASE_ERROR);
        }

        if (existingFollow != null) {
            try {
                existingFollow.getFollowingUser().getFollowings().remove(existingFollow);
                existingFollow.getFollowedUser().getFollowers().remove(existingFollow);
                followRepository.delete(existingFollow);
            } catch (Exception e) {
                throw new CustomException(ErrorCode.DATABASE_ERROR);
            }
            return false;
        } else {
            Follow follow = Follow.builder()
                    .followingUser(followingUser)
                    .followedUser(followedUser)
                    .build();

            followedUser.addFollower(follow);
            followingUser.addFollowing(follow);

            try {
                followRepository.save(follow);
            } catch (Exception e) {
                throw new CustomException(ErrorCode.DATABASE_ERROR);
            }
            return true;
        }
    }
}

