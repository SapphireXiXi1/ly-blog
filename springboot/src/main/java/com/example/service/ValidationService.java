package com.example.service;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.enums.ValidationEnum;
import com.example.entity.Validation;
import com.example.mapper.ValidationMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.apache.catalina.security.SecurityUtil.remove;

@Service
public class ValidationService extends ServiceImpl<ValidationMapper,Validation> implements ValidationServiceInterface{
    @Transactional
    public void saveCode(String email, String code, Integer type, DateTime expireDate) {
        Validation validation = new Validation();
        validation.setEmail(email);
        validation.setCode(code);
        validation.setType(type);
        validation.setTime(expireDate);

        //删除同类型的验证
        UpdateWrapper<Validation> validationUpdateWrapper = new UpdateWrapper<>();
        validationUpdateWrapper.eq("email",email);
        validationUpdateWrapper.eq("type", type);
        remove(validationUpdateWrapper);

        //再插入新的code
        save(validation);
    }
}
