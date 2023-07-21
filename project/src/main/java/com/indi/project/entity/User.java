package com.indi.project.entity;

import lombok.*;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
    @SequenceGenerator(name = "user_id_seq", sequenceName = "user_id_seq", initialValue = 1, allocationSize = 50)
    @Column(name = "user_id")
    private Long id;

    private String loginId;
    private String name;
    private String nickName;
    private String password;
    private Integer followerCnt;
    private LocalDateTime createAt;

    @OneToMany(mappedBy = "followingUser", cascade = CascadeType.ALL)
    private List<Follow> followings = new ArrayList<>();

    @OneToMany(mappedBy = "followedUser", cascade = CascadeType.ALL)
    private List<Follow> followers = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Video> videos = new ArrayList<>();

    @Builder
    public User(Long id, String loginId, String name, String nickName, String password, Integer followerCnt, LocalDateTime createAt, List<Follow> followings, List<Follow> followers, List<Video> videos) {
        this.id = id;
        this.loginId = loginId;
        this.name = name;
        this.nickName = nickName;
        this.password = password;
        this.followerCnt = followerCnt;
        this.createAt = createAt;
        this.followings = followings;
        this.followers = followers;
        this.videos = videos;
    }
}
