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

    public String getGenre() {
        return this.genre;
    }

    public static Genre fromString(String genre) {
        for (Genre g : Genre.values()) {
            if (g.getGenre().equalsIgnoreCase(genre)) {
                return g;
            }
        }
        throw new IllegalArgumentException("No constant with genre " + genre + " found");
    }
}
