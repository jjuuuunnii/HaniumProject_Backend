package com.indi.project.dto.exception;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorDto {
    private final boolean success;
    private final String code;

}
