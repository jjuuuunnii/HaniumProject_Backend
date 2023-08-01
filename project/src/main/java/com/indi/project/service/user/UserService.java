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

    @Value("${file.dir.profile}")
    private String fileDir;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserJoinResDto joinUser(UserJoinReqDto userJoinReqDto) {
        validationDuplicationId(userJoinReqDto.getLoginId(), userJoinReqDto.getNickName());
        User user = convertDtoToEntity(userJoinReqDto);
        userRepository.save(user);
        log.info("{} join success", user.getName());
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
    public void editUserInfo(String loginId, UserEditInfoDto userEditInfoDto) {
        User user = userRepository.findByLoginId(loginId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.setName(userEditInfoDto.getName());
        user.setNickName(userEditInfoDto.getNickName());
        if(userEditInfoDto.getProfileImage() == null){
            return;
        }

        if(userEditInfoDto.getProfileImage() != null && !userEditInfoDto.getProfileImage().isEmpty()){
            String oldProfileImageUrl = user.getProfileImageUrl();
            if(oldProfileImageUrl != null) {
                deleteFile(oldProfileImageUrl);
            }
            String newProfileImageUrl = storeFile(userEditInfoDto.getProfileImage());
            user.setProfileImageUrl(newProfileImageUrl);
        }
    }

    @Transactional
    public String storeFile(MultipartFile file) {
        try {
            String fileName = getFileName(file);
            String filePath = fileDir + File.separator + fileName;

            file.transferTo(new File(filePath));

            return filePath;
        } catch (IOException e) {
            throw new CustomException(ErrorCode.STORE_FILES_FAILS);
        }
    }
    public boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            if (!file.delete()) {
                throw new CustomException(ErrorCode.FILE_DELETION_FAILED);
            }
            return true;
        }
        return false;
    }


    private static String getFileName(MultipartFile videoFile) {
        String videoFileName = UUID.randomUUID() + "_" + videoFile.getOriginalFilename();
        return videoFileName;
    }

    private User convertDtoToEntity(UserJoinReqDto userJoinReqDto) {
        return User.builder()
                .loginId(userJoinReqDto.getLoginId())
                .password(passwordEncoder.encode(userJoinReqDto.getPassword()))
                .nickName(userJoinReqDto.getNickName())
                .name(userJoinReqDto.getName())
                .createAt(LocalDateTime.now())
                .profileImageUrl(fileDir + "defaultProfile.png")
                .build();
    }

    private User findUserByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private void validationDuplicationId(String loginId, String nickName) {
        Optional<User> byLoginIdUser = userRepository.findByLoginId(loginId);
        Optional<User> byNickNameUser = userRepository.findByNickName(nickName);

        if(byLoginIdUser.isPresent() && byNickNameUser.isPresent()){
            throw new CustomException(ErrorCode.ID_AND_NICKNAME_DUPLICATION);
        }
        if(byNickNameUser.isPresent()){
            throw new CustomException(ErrorCode.NICKNAME_DUPLICATION);
        }
        if(byLoginIdUser.isPresent()){
            throw new CustomException(ErrorCode.ID_DUPLICATION);
        }
    }


}

