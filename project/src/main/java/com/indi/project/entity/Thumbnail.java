package com.indi.project.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Thumbnail {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "thumbnail_id_seq")
    @SequenceGenerator(name = "thumbnail_id_seq", sequenceName = "thumbnail_id_seq", initialValue = 1, allocationSize = 50)
    @Column(name = "thumbnail_id")
    private Long id;

    @OneToOne(mappedBy ="thumbnail", fetch = FetchType.LAZY)
    private Video video;

    private String name;
    private String img;
}
