package com.indi.project.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "video_id_seq")
    @SequenceGenerator(name = "video_id_seq", sequenceName = "video_id_seq", initialValue = 1, allocationSize = 50)
    @Column(name = "video_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL)
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL)
    private List<View> views = new ArrayList<>();

    private String videoPath;
    private String thumbNailPath;
    private String title;
    private LocalDateTime createdAt;

    //private int viewsByTime;

    public Video(){}
    public int totalLikesCnt(){
        return likes.size();
    }



}
