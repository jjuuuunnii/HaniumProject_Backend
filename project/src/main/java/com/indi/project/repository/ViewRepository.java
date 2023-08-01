package com.indi.project.repository;

import com.indi.project.entity.User;
import com.indi.project.entity.Video;
import com.indi.project.entity.View;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.swing.text.html.parser.Entity;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ViewRepository {

    private final EntityManager em;

    public void save(View view){
        em.persist(view);
    }

    public Optional<View> findViewByUserAndVideo(User user, Video video) {
        return em.createQuery("SELECT v FROM View v WHERE v.user = :user AND v.video = :video", View.class)
                .setParameter("user", user)
                .setParameter("video", video)
                .getResultList().stream().findFirst();
    }

}
