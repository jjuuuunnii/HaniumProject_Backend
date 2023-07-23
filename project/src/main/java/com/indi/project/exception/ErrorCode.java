package com.indi.project.exception;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public enum  ErrorCode {

    NOT_BLANK_NAME("ERROR_CODE_BLANK_NAME","이름이 누락되었습니다."),
    NOT_BLANK_NICKNAME("ERROR_CODE_BLANK_NICKNAME","닉네임이 누락되었습니다."),
    NOT_BLANK_LOGINID("ERROR_CODE_BLANK_NAME","ID가 누락되었습니다."),
    NOT_BLANK_PASSWORD("ERROR_CODE_BLANK_NAME","비밀번호가 누락되었습니다."),
    PATTERN("ERROR_CODE_PATTERN", "값 형식이 다릅니다.");


    private String code;
    private String description;

    ErrorCode(String code, String description) {
        this.code = code;
        this.description = description;
    }
}