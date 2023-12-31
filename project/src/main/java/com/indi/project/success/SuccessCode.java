package com.indi.project.success;

import lombok.Getter;

@Getter
public enum SuccessCode {

    COMMENT_POSTED(true, "COMMENT POSTED"),
    LIKE_STATUS_POSTED(true, "LIKE STATUS POSTED"),
    VIDEO_POSTED(true, "VIDEO POSTED"),
    VIEW_INCREASED_POSTED(true, "VIEW INCREASE POSTED"),
    FOLLOW_POSTED(true, "FOLLOW POSTED"),
    USER_DELETED(true, "USER DELETED"),
    USER_EDITED(true, "USER EDITED"),
    VIDEO_DELETED(true, "VIDEO DELETED"),
    LOGOUT_SUCCESS(true, "LOGOUT SUCCESS"),
    COMMENT_DELETED(true, "COMMENT DELETED");

    private final boolean success;
    private final String code;

    SuccessCode(boolean success, String code) {
        this.success = success;
        this.code = code;
    }
}
