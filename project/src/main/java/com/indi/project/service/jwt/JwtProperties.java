package com.indi.project.service.jwt;

public interface JwtProperties {
    String SECRET_KEY = "Indi-SERVER-JWT-TOKEN";
    int EXPIRATION_TIME = 432_000_000; // 5 days
    String TOKEN_PREFIX = "Bearer ";
    String ACCESS_TOKEN = "Indi-SERVER-JWT-TOKEN";
    String ACCESS_HEADER_PREFIX = "Authorization";
    String LOGIN_ID = "LoginId";
    String USERNAME = "Username";
    String REFRESH_TOKEN = "Indi-SERVER-JWT-REFRESH-TOKEN";
    String REFRESH_HEADER_PREFIX = "ReAuthorization";
    Long REFRESH_EXPIRATION_TIME = (1000L * 60 * 60 * 24 * 30); // 30 days
    String EXCEPTION = "비로그인 사용자 접속 요청";

}
