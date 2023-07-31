package com.indi.project.repository;
import com.indi.project.entity.Like;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;


@Slf4j
@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeRepository {

    private final EntityManager em;

    @Transactional
    public Long save(Like like){
        List<Like> existingLikes = em.createQuery(
                        "select l from Like l where l.video.id = :videoId and l.user.id = :userId", Like.class)
                .setParameter("videoId", like.getVideo().getId())
                .setParameter("userId", like.getUser().getId())
                .getResultList();

        if (existingLikes.isEmpty()) {
            em.persist(like);
        } else {
            Like existingLike = existingLikes.get(0);
            existingLike.setLikeStatus(like.isLikeStatus());
        }

        return like.getId();
    }
}
