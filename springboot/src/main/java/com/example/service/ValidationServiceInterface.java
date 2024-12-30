package com.example.service;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.Validation;

public interface ValidationServiceInterface extends IService<Validation> {
    void saveCode(String email, String code, Integer type, DateTime expireDate);
}
