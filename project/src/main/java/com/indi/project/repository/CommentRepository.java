package com.indi.project.repository;

import com.indi.project.entity.Comment;
import com.indi.project.entity.Video;
import com.indi.project.exception.CustomException;
import com.indi.project.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentRepository {

    private final EntityManager em;

    @Transactional
    public Long save(Comment comment) {
        log.info("save comment = {}", comment);
        em.persist(comment);
        return comment.getId();
    }

    public List<Comment> findByVideoId(Long videoId) {
        return em.createQuery("select c from Comment c where c.video.id = :videoId", Comment.class)
                .setParameter("videoId", videoId)
                .getResultList();
    }

    @Transactional
    public void deleteComments(Long videoId, Long userId, LocalDateTime localDateTime) {
        log.info("Deleting comments for user with id = {} and videoId = {} at {}", userId, videoId, localDateTime);
        int deletedCount = em.createQuery("delete from Comment c where c.user.id = :userId and c.video.id = :videoId and c.createAt = :localDateTime")
                .setParameter("userId", userId)
                .setParameter("videoId", videoId)
                .setParameter("localDateTime", localDateTime)
                .executeUpdate();
        log.info("삭제한 댓글 개수: {}", deletedCount);
    }
}
