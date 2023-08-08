package com.indi.project.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="likes",uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "video_id"}))
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "like_id_seq")
    @SequenceGenerator(name = "like_id_seq", sequenceName = "like_id_seq", initialValue = 1, allocationSize = 50)
    @Column(name = "like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id")
    private Video video;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    public Like(){}
}

