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

    public void save(Like like){
        em.persist(like);
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

    public void deleteByUserIdAndVideoId(Long userId, Long videoId) {
        Optional<Like> likeOpt = findByUserIdAndVideoId(userId, videoId);
        if(likeOpt.isPresent()) {
            em.remove(likeOpt.get());
        }
    }

}
