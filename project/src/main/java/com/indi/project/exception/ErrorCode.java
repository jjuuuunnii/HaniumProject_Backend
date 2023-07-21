package com.indi.project.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    //400
    NOT_FOUND_USER(400, "NO USER EXIST");


    private final int status;
    private final String message;
}
