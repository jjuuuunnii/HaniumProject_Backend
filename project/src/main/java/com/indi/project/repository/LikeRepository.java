package com.indi.project.repository;
import com.indi.project.entity.Like;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;


@Slf4j
@Repository
@RequiredArgsConstructor
public class LikeRepository {

    private final EntityManager em;

    public boolean save(Like like){
        List<Like> existingLikes = em.createQuery(
                        "select l from Like l where l.video.id = :videoId and l.user.id = :userId", Like.class)
                .setParameter("videoId", like.getVideo().getId())
                .setParameter("userId", like.getUser().getId())
                .getResultList();

        if(existingLikes.isEmpty()){
            like.setLikeStatus(true);
            em.persist(like);
        }else{
            Like existingLike = existingLikes.get(0);
            existingLike.setLikeStatus(!existingLike.isLikeStatus());
        }

        return existingLikes.get(0).isLikeStatus();
    }

    public Optional<Like> findByUserIdAndVideoId(Long userId, Long videoId) {
        List<Like> results = em.createQuery(
                        "select l from Like l where l.user.id = :userId and l.video.id = :videoId", Like.class)
                .setParameter("userId", userId)
                .setParameter("videoId", videoId)
                .getResultList();

        if(results.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(results.get(0));
        }
    }

}
