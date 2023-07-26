package com.indi.project.exception.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtException extends RuntimeException{
    private JwtErrorCode jwtErrorCode;
}
