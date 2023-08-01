package com.indi.project.exception;
import lombok.Getter;



@Getter
public enum ErrorCode {
    //400
    NULL_POINT_EXCEPTION(false, "NULL POINT EXCEPTION"),
    ID_DUPLICATION(false, "ID DUPLICATION"),
    NICKNAME_DUPLICATION(false, "NICKNAME DUPLICATION"),
    ID_AND_NICKNAME_DUPLICATION(false, "ID AND NICKNAME DUPLICATION"),

    //401
    ACCESS_TOKEN_EXPIRED(false,"TOKEN EXPIRED"),
    ILLEGAL_REFRESH_TOKEN(false, "ILLEGAL_REFRESH_TOKEN"),
    ILLEGAL_ACCESS_TOKEN(false, "ILLEGAL_REFRESH_TOKEN"),

    //404
    USER_NOT_FOUND(false,"USER NOT FOUND"),
    GET_VIDEOS_INFO_FAIL(false, "GET VIDEOS INFO FAIL"),
    GET_VIDEO_INFO_FAIL(false, "GET VIDEO INFO FAIL"),
    VIDEO_NOT_FOUND(false, "VIDEO NOT FOUND"),

    //500
    AUTHENTICATION_ERROR_NO_USER(false,"AUTHENTICATION ERROR NO USER"),
    STORE_FILES_FAILS(false,"STORE FILES_FAILS"),
    CANNOT_FOLLOW_SELF(false,"CANNOT FOLLOW SELF"),
    DATABASE_ERROR(false, "DATABASE ERROR"),
    PERMISSION_DENIED(false, "PERMISSION DENIED"),
    FILE_DELETION_FAILED(false,"FILE DELETION FAILED"),
    LOGOUT_FAILED(false, "LOGIN FAILED");

    private final boolean success;
    private final String code;
    ErrorCode(boolean success, String code) {
        this.success = success;
        this.code = code;
    }
}