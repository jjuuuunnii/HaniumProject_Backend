package com.indi.project.service.comment;

import com.indi.project.dto.commet.req.CommentDeleteReqDto;
import com.indi.project.dto.commet.req.CommentReqDto;
import com.indi.project.dto.like.LikesDto;
import com.indi.project.entity.Comment;
import com.indi.project.entity.User;
import com.indi.project.exception.CustomException;
import com.indi.project.exception.ErrorCode;
import com.indi.project.repository.CommentRepository;
import com.indi.project.repository.UserRepository;
import com.indi.project.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;
    private final VideoRepository videoRepository;
    private final UserRepository userRepository;

    @Transactional
    public void saveComments(Long id, CommentReqDto commentReqDto) {
        User user = userRepository.findByLoginId(commentReqDto.getLoginId()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        commentRepository.save(commentDtoToComment(id, commentReqDto, user));
    }

    @Transactional
    public void deleteComments(Long videoId, CommentDeleteReqDto commentDeleteReqDto) {
        Optional<User> user = userRepository.findByLoginId(commentDeleteReqDto.getLoginId());
        if(user.isEmpty()){
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        commentRepository.deleteComments(videoId, user.get().getId(), commentDeleteReqDto.getTime());
    }

    private Comment commentDtoToComment(Long id, CommentReqDto commentReqDto, User user) {
        Comment comment = Comment.builder()
                .content(commentReqDto.getContent())
                .createAt(LocalDateTime.now())
                .build();

        comment.setUser(user);
        comment.setVideo(videoRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.VIDEO_NOT_FOUND)));
        user.getComments().add(comment);
        return comment;
    }
}
