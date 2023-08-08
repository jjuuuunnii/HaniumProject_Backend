package com.indi.project.entity;

import com.indi.project.dto.commet.req.CommentReqDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "comment_id_seq")
    @SequenceGenerator(name = "comment_id_seq", sequenceName = "comment_id_seq", initialValue = 1, allocationSize = 50)
    @Column(name = "comment_id")
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id")
    private Video video;

    private LocalDateTime createAt;
    public Comment(){}

}
