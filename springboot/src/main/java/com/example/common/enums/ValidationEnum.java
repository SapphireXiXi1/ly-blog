package com.example.common.enums;

public enum ValidationEnum {
    LOGIN(1),  //登录
    FORGET_PASS(2);  //忘记密码
    private Integer code;

    public Integer getCode() {
        return code;
    }

    ValidationEnum(Integer code) {
        this.code = code;
    }
}
