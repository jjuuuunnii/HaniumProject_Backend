package com.indi.project.repository;

import com.indi.project.entity.Follow;
import com.indi.project.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FollowRepository {

    private final EntityManager em;

    public Long save(Follow follow){
        em.persist(follow);
        return follow.getId();
    }

    public void delete(Follow follow) {
        em.remove(follow);
    }

    public Follow findFollowRelation(User followingUser, User followedUser){
        try {
            return em.createQuery("SELECT f FROM Follow f WHERE f.followingUser = :followingUser AND f.followedUser = :followedUser", Follow.class)
                    .setParameter("followingUser", followingUser)
                    .setParameter("followedUser", followedUser)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
