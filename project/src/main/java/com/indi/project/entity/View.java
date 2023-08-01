package com.indi.project.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
public class View {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "view_id_seq")
    @SequenceGenerator(name = "view_id_seq", sequenceName = "view_id_seq", initialValue = 1, allocationSize = 50)
    @Column(name = "view_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="video_id")
    private Video video;

    public View(){}
}
