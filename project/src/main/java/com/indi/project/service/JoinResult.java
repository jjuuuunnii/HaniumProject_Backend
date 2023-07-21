package com.indi.project.service;

public interface JoinResult {
    public String JOIN_SUCCESS = "success";
    public String DUPLICATION_ERROR_ID = "failById";
    public String DUPLICATION_ERROR_NICKNAME = "failByNickName";
    public String DUPLICATION_ERROR_BOTH = "failByIdAndNickName";
}
