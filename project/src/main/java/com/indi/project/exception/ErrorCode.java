package com.indi.project.exception;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public enum ErrorCode {

    USER_NOT_FOUND(404,"USER NOT FOUND", "User Not Found"),
    ID_DUPLICATION(400, "ID DUPLICATION", "Duplicated Id"),
    NICKNAME_DUPLICATION(400, "NICKNAME DUPLICATION", "Duplicated NickName"),
    ID_AND_NICKNAME_DUPLICATION(400, "ID AND NICKNAME DUPLICATION", "Duplicated Id Both NickName"),
    AUTHENTICATION_ERROR_NO_USER(500,"AUTHENTICATION ERROR NO USER", "LoadUserByUsername Error");

    private final int status;
    private final String code;
    private final String description;

    ErrorCode(int status, String code,String description) {
        this.status = status;
        this.code = code;
        this.description = description;
    }
}