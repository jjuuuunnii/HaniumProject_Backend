package com.indi.project.repository;

import com.indi.project.entity.User;
import com.indi.project.entity.Video;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
@ToString
public class UserRepository {

    private final EntityManager em;

    //save
    //findById
    //findAll
    //deleteById

    public Long save(User user) {
        log.info("save user = {}", user);
        em.persist(user);
        return user.getId();
    }

    public Optional<User> findById(Long id) {
        User user = em.find(User.class, id);
        return Optional.ofNullable(user);
    }

    //이름은 중복 가능
    public Optional<List> findByName(String name) {
        try {
            List<User> users = em.createQuery("select u from User u where u.name = :name", User.class)
                    .setParameter("name", name)
                    .getResultList();
            return Optional.ofNullable(users);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    //로그인 아이디 중복 불가능
    public Optional<User> findByLoginId(String loginId) {
        try {
            User user = em.createQuery("select u from User u where u.loginId = :loginId", User.class)
                    .setParameter("loginId", loginId)
                    .getSingleResult();
            return Optional.ofNullable(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    //닉네임 중복 불가능
    public Optional<User> findByNickName(String nickName) {
        try {
            User user = em.createQuery("select u from User u where u.nickName = :nickName", User.class)
                    .setParameter("nickName", nickName)
                    .getSingleResult();
            return Optional.ofNullable(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public List<User> findAll(){
        return em.createQuery("select u from User u", User.class)
                .getResultList();
    }

    public void deleteById(Long id) {
        Optional<User> user = findById(id);
        user.ifPresentOrElse(
                em::remove,
                () -> {
                    throw new InvalidDataAccessApiUsageException("No user with id " + id);
                });
    }

    public Optional<User> findByRefreshToken(String refreshToken) {
        List<User> user = em.createQuery("select u from User u where u.refreshToken = :refreshToken", User.class)
                .setParameter("refreshToken", refreshToken)
                .getResultList();

        return user.stream().findAny();
    }

    public Optional<User> findByLoginIdAndPassword(String loginId, String password) {
        List<User> resultList = em.createQuery("select u from User u where u.loginId = :loginId and u.password = :password", User.class)
                .setParameter("loginId", loginId)
                .setParameter("password", password)
                .getResultList();
        if(resultList.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(resultList.get(0));
        }
    }

    public Optional<List> findVideosByUserLoginId(String loginId) {
        return Optional.ofNullable(findByLoginId(loginId).get().getVideos());
    }

}
