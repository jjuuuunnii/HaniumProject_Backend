package com.indi.project.service.user;
import com.indi.project.dto.user.req.UserJoinReqDto;
import com.indi.project.dto.user.res.UserJoinResDto;
import com.indi.project.entity.User;
import com.indi.project.exception.CustomException;
import com.indi.project.exception.ErrorCode;
import com.indi.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Slf4j
@Service
@RequiredArgsConstructor
@ToString
@Transactional(readOnly = true)
public class UserService {

    @Value("${file.dir.profile}")
    private final String fileDir;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public UserJoinResDto joinUser(UserJoinReqDto userJoinReqDto) {
        validationDuplicationId(userJoinReqDto.getLoginId(), userJoinReqDto.getNickName());
        User user = userJoinReqDto.toEntity(passwordEncoder.encode(userJoinReqDto.getPassword()));
        user.setProfileImageUrl(fileDir + "defaultProfile.png");
        userRepository.save(user);
        log.info("{} join success", user.getName());
        return new UserJoinResDto(true, "SIGNUP SUCCESS");
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

