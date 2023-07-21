package com.indi.project.repository;

import com.indi.project.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRepositoryTest {
    @Autowired UserRepository userRepository;
    @Autowired EntityManager em;

    public void createTestMember(){

    }

    @Test
    @Transactional
    void save() {
    }

    @Test
    void findById() {

/*        User user1 = User.builder()
                .loginId("jake99111110")
                .name("kkang")
                .nickName("jjjjuuuunnii")
                .password("kkkang8092")
                .build();
        Long savedUserId1 = userRepository.save(user1);
        User byId = userRepository.findById(savedUserId1);
        em.flush();
        em.clear();


        assertThat(byId).isEqualTo(user1);*/
    }

    @Test
    void findByLoginId() {
    }

    @Test
    void findByName() {
    }

    @Test
    void findAll() {
        User user1 = User.builder()
                .loginId("jake99111110")
                .name("kkang")
                .nickName("jjjjuuuunnii")
                .password("kkkang8092")
                .build();

        User user2 = User.builder()
                .loginId("jake991110")
                .name("kang")
                .nickName("jjuuuunnii")
                .password("kang8092")
                .build();
        Long savedUserId1 = userRepository.save(user1);
        Long savedUserId2 = userRepository.save(user2);
        em.flush();
        em.clear();

        List<User> users = userRepository.findAll();
        assertThat(users).contains(user1,user2);

    }

    @Test
    @Transactional
    void deleteById() {
        //유저가 있는 경우
        User user1 = User.builder()
                .loginId("jake99111110")
                .name("kkang")
                .nickName("jjjjuuuunnii")
                .password("kkkang8092")
                .build();
        Long savedUserId1 = userRepository.save(user1);
        em.flush();
        em.clear();
        userRepository.deleteById(savedUserId1);
        List<User> all = userRepository.findAll();
        assertThat(all.size()).isEqualTo(0);
        //유저가 없는 경우
        assertThrows(InvalidDataAccessApiUsageException.class, () -> userRepository.deleteById(savedUserId1));
    }


}