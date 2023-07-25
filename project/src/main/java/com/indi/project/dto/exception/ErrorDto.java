package com.indi.project.dto.exception;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorDto {
    private final int status;
    private final String code;
    private final String description;
}
