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
    AUTHENTICATION_ERROR_NO_USER(500,"AUTHENTICATION ERROR NO USER", "LoadUserByUsername Error"),
    /**
     * TODO
     *
     * 토큰 재발급하기
     * 구현해야함
     */
    //내부에서 알아서 처리함
    ILLEGAL_REFRESH_TOKEN(401, "ILLEGAL_REFRESH_TOKEN", "Illegal Refresh Token"),
    //ACCESSTOKEN 컨트롤러 따로 만듦
    ILLEGAL_ACCESS_TOKEN(401, "ILLEGAL_REFRESH_TOKEN", "Illegal Refresh Token");

    private final int status;
    private final String code;
    private final String description;

    ErrorCode(int status, String code,String description) {
        this.status = status;
        this.code = code;
        this.description = description;
    }
}