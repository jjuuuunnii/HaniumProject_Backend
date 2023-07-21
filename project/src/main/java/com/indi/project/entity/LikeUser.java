package com.indi.project.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
public class LikeUser {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "likeUser_id_seq")
    @SequenceGenerator(name = "likeUser_id_seq", sequenceName = "likeUser_id_seq", initialValue = 1, allocationSize = 50)
    @Column(name = "likeUser_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="like_id")
    private Like like;
}
