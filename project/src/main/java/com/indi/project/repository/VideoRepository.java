package com.indi.project.repository;

import com.indi.project.entity.Genre;
import com.indi.project.entity.User;
import com.indi.project.entity.Video;
import com.indi.project.exception.CustomException;
import com.indi.project.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
@ToString
@Transactional(readOnly = true)
public class VideoRepository {


    //save
    //findById
    //findAll
    //deleteById

    private final EntityManager em;
    @Transactional
    public Long save(Video video) {
        log.info("save video = {}", video);
        em.persist(video);
        return video.getId();
    }

    @Transactional(readOnly = true)
    public Optional<Video> findById(Long id) {
        Video video = em.find(Video.class, id);
        return Optional.ofNullable(video);
    }

    @Transactional(readOnly = true)
    public List<Video> findByGenre(Genre genre) {
        List<Video> videos = em.createQuery("select v from Video v where v.genre = :genre", Video.class)
                .setParameter("genre", genre)
                .getResultList();

        if (videos.isEmpty()) {
            throw new CustomException(ErrorCode.GET_VIDEOS_INFO_FAIL);
        }
        return videos;
    }


    @Transactional
    public void deleteById(Long id) {
        Optional<Video> video = findById(id);
        video.ifPresentOrElse(
                em::remove,
                () -> {
                    throw new InvalidDataAccessApiUsageException("No user with id " + id);
                });
    }

}
