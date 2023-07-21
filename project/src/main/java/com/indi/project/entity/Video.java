package com.indi.project.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "video_id_seq")
    @SequenceGenerator(name = "video_id_seq", sequenceName = "video_id_seq", initialValue = 1, allocationSize = 50)
    @Column(name = "video_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Lob
    private byte[] videoFile;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "thumbnail_id")
    private Thumbnail thumbnail;

    /*@ManyToOne
    @JoinColumn(name = "graphic_id")
    private Graphic graphic;

    @OneToMany(mappedBy = "video")
    private List<VideosGraphics> videosGraphics;
*/
    private int likeCnt;
    private String title;
    private int viewCnt;
    private int viewsByTime;
    private LocalDateTime createdAt;


}
