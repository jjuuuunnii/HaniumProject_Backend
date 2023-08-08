package com.indi.project.entity;

import com.indi.project.dto.user.req.FollowingDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "follow_id_seq")
    @SequenceGenerator(name = "follow_id_seq", sequenceName = "follow_id_seq", initialValue = 1, allocationSize = 50)
    @Column(name = "follow_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_user_id")
    private User followingUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followed_user_id")
    private User followedUser;

    public Follow(){}

    public void setFollowingUser(User user) {
        if(this.followingUser != null) {
            this.followingUser.getFollowings().remove(this);
        }
        this.followingUser = user;
        user.getFollowings().add(this);
    }

    public void setFollowedUser(User user) {
        if(this.followedUser != null) {
            this.followedUser.getFollowers().remove(this);
        }
        this.followedUser = user;
        user.getFollowers().add(this);
    }
}
