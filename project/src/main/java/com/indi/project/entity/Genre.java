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
    INDIMUSIC("IndiMusic"),
    ROCKMETAL("RockMetal"),
    RNBSOUL("RnBSoul"),
    POP("Pop"),
    BALLAD("Ballad"),
    RAPHIPHOP("RapHiphop");

    private final String genre;

    Genre(String genre) {
        this.genre = genre;
    }
}
