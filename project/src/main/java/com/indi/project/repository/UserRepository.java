package com.indi.project.repository;

import com.indi.project.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final EntityManager em;

    //save
    //findById
    //findAll
    //deleteById

    public Long save(User user) {
        em.persist(user);
        return user.getId();
    }

    public User findById(Long id) {
        return em.find(User.class, id);
    }

    public List<User> findByLoginId(String loginId) {
        return em.createQuery(
                        "select u from User u where u.loginId = :loginId")
                .setParameter("loginId", loginId)
                .getResultList();
    }

    public List<User> findByName(String name) {
        return em.createQuery(
                        "select u from User u where u.name = :name")
                .setParameter("name", name)
                .getResultList();
    }

    public List<User> findAll(){
        return em.createQuery("select u from User u", User.class)
                .getResultList();
    }

    public void deleteById(Long id) {
        User user = em.find(User.class, id);
        if (user == null) {
            throw new InvalidDataAccessApiUsageException("No user with id " + id);
        }
        em.remove(user);
    }


}
