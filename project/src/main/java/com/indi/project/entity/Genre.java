package com.indi.project.entity;

public enum Genre {
    /**
     * 인디뮤직
     * 락/메탈
     * RNB soul
     * POP
     * 발라드
     * 랩/힙합
     */

    INDI("인디"),
    ROCK_AND_METAL("락메탈"),
    RNB_SOUL("알앤비"),
    POP("팝"),
    BALLAD("발라드"),
    RAP_HIPHOP("랩힙합");

    private String genre;

    Genre(String genre) {
        this.genre = genre;
    }
}
