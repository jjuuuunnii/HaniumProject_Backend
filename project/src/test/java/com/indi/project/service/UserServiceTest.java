/*
package com.indi.project.service;

import com.indi.project.dto.user.req.UserJoinReqDto;
import com.indi.project.entity.User;
import com.indi.project.repository.UserRepository;
import com.indi.project.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired UserRepository userRepository;
    @Autowired
    UserService userService;
//    @Autowired PasswordEncoder passwordEncoder;
    @Autowired EntityManager em;

    @Test
    @Transactional
    void joinUser() {
        UserJoinReqDto userJoinReqDto = UserJoinReqDto.builder()
                .name("kang")
                .nickName("jjuunnii")
                .loginId("jake991110")
                .password("kang80892")
                .build();
        assertThat(userService.joinUser(userJoinReqDto).getMessage()).isEqualTo("success");
    }

    @Test
    @Transactional
    void duplicationTest() {

        User user1 = User.builder()
                .loginId("jake9911110")
                .name("kang")
                .nickName("jjuuuunnii")
                .password("kang8092")
                .build();

        userRepository.save(user1);
        em.flush();
        em.clear();

        //아이디 중복 검증
        UserJoinReqDto userJoinReqDto1 = UserJoinReqDto.builder()
                .name("kang")
                .nickName("test")
                .loginId("jake991110")
                .password("kang80892")
                .build();
        assertThrows(IllegalStateException.class,()->userService.joinUser(userJoinReqDto1));

        //닉네임 중복 검증
        UserJoinReqDto userJoinReqDto2 = UserJoinReqDto.builder()
                .name("kang")
                .nickName("jjuuuunnii")
                .loginId("test")
                .password("kang80892")
                .build();
        assertThrows(IllegalStateException.class,()->userService.joinUser(userJoinReqDto2));

        //두개 중복 검증
        UserJoinReqDto userJoinReqDto3 = UserJoinReqDto.builder()
                .name("kang")
                .nickName("jjuuuunnii")
                .loginId("jake991110")
                .password("kang80892")
                .build();
        assertThrows(IllegalStateException.class,()->userService.joinUser(userJoinReqDto3));
    }

}*/
