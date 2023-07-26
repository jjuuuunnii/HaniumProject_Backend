package com.indi.project.exception.jwt;

import lombok.Getter;

@Getter
public enum JwtErrorCode {
    ACCESS_TOKEN_EXPIRED(401,"TOKEN EXPIRED","Access Token Expired"),
    ILLEGAL_REFRESH_TOKEN(401, "ILLEGAL_REFRESH_TOKEN", "Illegal Refresh Token"),
    ILLEGAL_ACCESS_TOKEN(401, "ILLEGAL_REFRESH_TOKEN", "Illegal Refresh Token");

    private final int status;
    private final String code;
    private final String description;

    JwtErrorCode(int status, String code, String description) {
        this.status = status;
        this.code = code;
        this.description = description;
    }
}
