package com.indi.project.service.user;

import com.indi.project.dto.mypage.FollowingListDto;
import com.indi.project.dto.mypage.LikeListDto;
import com.indi.project.dto.mypage.GetMyPageDto;
import com.indi.project.dto.user.req.UserEditInfoDto;
import com.indi.project.dto.user.req.UserJoinReqDto;
import com.indi.project.dto.user.req.UserLeaveDto;
import com.indi.project.dto.user.res.UserJoinResDto;
import com.indi.project.entity.User;
import com.indi.project.exception.CustomException;
import com.indi.project.exception.ErrorCode;
import com.indi.project.repository.UserRepository;
import com.indi.project.service.file.FileService;
import com.indi.project.service.studio.StudioService;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    @Value("${cloud.aws.s3.profile-folder}")
    private String profileDir;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileService fileService;

    @Transactional
    public UserJoinResDto joinUser(UserJoinReqDto userJoinReqDto) {
        validationDuplicationId(userJoinReqDto.getLoginId(), userJoinReqDto.getNickName());
        User user = convertDtoToEntity(userJoinReqDto);
        userRepository.save(user);
        log.info("{} join success", user.getName());
        log.info("userProfilePath = {}", user.getProfileImageUrl());

        return new UserJoinResDto(true, "SIGNUP SUCCESS");
    }

    public GetMyPageDto getMyPageInfo(String loginId) {
        User user = findUserByLoginId(loginId);

        List<FollowingListDto> followingList = user.getFollowings().stream()
                .map(follow -> new FollowingListDto(
                        follow.getFollowingUser().getLoginId(),
                        follow.getFollowingUser().getNickName(),
                        follow.getFollowingUser().getProfileImageUrl()
                ))
                .collect(Collectors.toList());

        List<LikeListDto> likeList = user.getLikes().stream()
                .map(like -> new LikeListDto(
                        like.getVideo().getThumbNailPath(),
                        like.getVideo().getTitle(),
                        like.getVideo().getUser().getNickName()
                ))
                .collect(Collectors.toList());

        return GetMyPageDto.builder()
                .name(user.getName())
                .loginId(user.getLoginId())
                .nickName(user.getNickName())
                .following(followingList)
                .like(likeList)
                .profileImageUrl(user.getProfileImageUrl())
                .build();
    }

    @Transactional
    public void leaveUser(UserLeaveDto userLeaveDto) {
        User user = findUserByLoginId(userLeaveDto.getLoginId());
        userRepository.deleteById(user.getId());
    }

    @Transactional
    public void editUserInfo(String loginId, UserEditInfoDto userEditInfoDto) throws IOException {
        User user = userRepository.findByLoginId(loginId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.setName(userEditInfoDto.getName());
        user.setNickName(userEditInfoDto.getNickName());

        if (userEditInfoDto.getProfileImage() == null) { return; }

        String oldProfileImageUrl = user.getProfileImageUrl();
        if (!oldProfileImageUrl.equals("profile/defaultProfile.png")) {
            fileService.deleteFile(oldProfileImageUrl);
        }

        String newProfileImageUrl = fileService.saveFile(userEditInfoDto.getProfileImage(), profileDir);
        log.info("newProfileImageUrl = {} ", newProfileImageUrl);
        user.setProfileImageUrl(newProfileImageUrl);
    }


    private User convertDtoToEntity(UserJoinReqDto userJoinReqDto) {
        return User.builder()
                .loginId(userJoinReqDto.getLoginId())
                .password(passwordEncoder.encode(userJoinReqDto.getPassword()))
                .nickName(userJoinReqDto.getNickName())
                .name(userJoinReqDto.getName())
                .createAt(LocalDateTime.now())
                .profileImageUrl(profileDir + "/defaultProfile.png")
                .build();
    }

    private User findUserByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private void validationDuplicationId(String loginId, String nickName) {
        Optional<User> byLoginIdUser = userRepository.findByLoginId(loginId);
        Optional<User> byNickNameUser = userRepository.findByNickName(nickName);

        if (byLoginIdUser.isPresent() && byNickNameUser.isPresent()) {
            throw new CustomException(ErrorCode.ID_AND_NICKNAME_DUPLICATION);
        }
        if (byNickNameUser.isPresent()) {
            throw new CustomException(ErrorCode.NICKNAME_DUPLICATION);
        }
        if (byLoginIdUser.isPresent()) {
            throw new CustomException(ErrorCode.ID_DUPLICATION);
        }
    }


}

