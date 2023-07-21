package com.indi.project.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "follow_id_seq")
    @SequenceGenerator(name = "follow_id_seq", sequenceName = "follow_id_seq", initialValue = 1, allocationSize = 50)
    @Column(name = "follow_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_user_id")
    private User followingUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followed_user_id")
    private User followedUser;
}
