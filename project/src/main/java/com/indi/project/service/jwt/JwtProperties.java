package com.indi.project.service.jwt;

public interface JwtProperties {
    String SECRET_KEY = "Indi-SERVER-JWT-TOKEN";
    int EXPIRATION_TIME = 864_000_000; // 10 days
    String TOKEN_PREFIX = "Bearer ";
    String ACCESS_TOKEN = "Indi-SERVER-JWT-TOKEN";
    String ACCESS_HEADER_PREFIX = "Authorization";
    String LOGIN_ID = "LoginId";
    String USERNAME = "username";
    String REFRESH_TOKEN = "Indi-SERVER-JWT-REFRESH-TOKEN";
    String REFRESH_HEADER_PREFIX = "ReAuthorization";
    Long REFRESH_EXPIRATION_TIME = (1000L * 60 * 60 * 24 * 30); // 30 days
    String EXCEPTION = "JWT Error";

}
