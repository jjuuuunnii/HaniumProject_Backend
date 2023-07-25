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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;
@Slf4j
@Service
@RequiredArgsConstructor
@ToString
public class UserService implements JoinResult {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public UserJoinResDto joinUser(UserJoinReqDto userJoinReqDto) {
        validationDuplicationId(userJoinReqDto.getLoginId(), userJoinReqDto.getNickName());
        User user = userJoinReqDto.toEntity(passwordEncoder.encode(userJoinReqDto.getPassword()));
        userRepository.save(user);
        log.info("joinSuccess");
        return new UserJoinResDto(JOIN_SUCCESS);
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

