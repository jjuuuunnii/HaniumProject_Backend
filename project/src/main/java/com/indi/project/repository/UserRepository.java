package com.indi.project.repository;

import com.indi.project.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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

    public Optional<User> findByName(String name) {
        try {
            User user = em.createQuery("select u from User u where u.name = :name", User.class)
                    .setParameter("name", name)
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


}
