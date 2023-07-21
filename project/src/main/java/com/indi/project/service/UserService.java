package com.indi.project.service;


import com.indi.project.dto.user.req.UserJoinReqDto;
import com.indi.project.dto.user.res.UserJoinResDto;
import com.indi.project.entity.User;
import com.indi.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Slf4j
@Service
@RequiredArgsConstructor
@ToString
@Transactional(readOnly = true)
public class UserService implements JoinResult{

    private final UserRepository userRepository;


    @Transactional
    public UserJoinResDto joinUser(UserJoinReqDto userJoinReqDto) {
        validationDuplicationId(userJoinReqDto.getLoginId(), userJoinReqDto.getName());
        User user = userJoinReqDto.toEntity(userJoinReqDto.getPassword());
        Long savedUserId = userRepository.save(user);
        log.info("joinSuccess");
        return new UserJoinResDto(JOIN_SUCCESS);
    }

    private void validationDuplicationId(String loginId, String nickName) {
        Optional<User> byLoginIdUser = userRepository.findByLoginId(loginId);
        Optional<User> byNickNameUser = userRepository.findByName(nickName);

        if(byLoginIdUser.isPresent() && byNickNameUser.isPresent()){
            throw new IllegalArgumentException(DUPLICATION_ERROR_BOTH);
        }
        if(byNickNameUser.isPresent()){
            throw new IllegalStateException(DUPLICATION_ERROR_NICKNAME);
        }
        if(byLoginIdUser.isPresent()){
            throw new IllegalStateException(DUPLICATION_ERROR_ID);
        }
    }
}

