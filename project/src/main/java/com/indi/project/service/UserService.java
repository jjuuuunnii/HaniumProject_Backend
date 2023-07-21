package com.indi.project.service;


import com.indi.project.dto.user.req.UserJoinReqDto;
import com.indi.project.dto.user.res.UserJoinResDto;
import com.indi.project.entity.User;
import com.indi.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserJoinResDto joinUser(UserJoinReqDto userJoinReqDto) {
        validationDuplicationId(userJoinReqDto.getLoginId(), userJoinReqDto.getName());
        User user = userJoinReqDto.toEntity(passwordEncoder.encode(userJoinReqDto.getPassword()));
        userRepository.save(user);
        return new UserJoinResDto();
    }

    private void validationDuplicationId(String loginId, String nickName) {
        List<User> byLoginIdUsers = userRepository.findByLoginId(loginId);
        List<User> byNickNameUsers = userRepository.findByName(nickName);
        if(!(byNickNameUsers.isEmpty()) || !(byLoginIdUsers.isEmpty()) ){
            throw new IllegalStateException("이미 존재하는 회원입니다");
        }
    }


}
