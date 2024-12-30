package com.example.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.common.Constants;
import com.example.common.Result;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.User;
import com.example.entity.Validation;
import com.example.exception.CustomException;
import com.example.mapper.UserMapper;
import com.example.service.AdminService;
import com.example.service.UserService;
import com.example.service.ValidationService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 基础前端接口
 */
@RestController
@CrossOrigin
public class WebController {

    @Resource
    private AdminService adminService;

    @Resource
    private UserService userService;

    @Resource
    private ValidationService validationService;

    @Resource
    private UserMapper userMapper;

    @GetMapping("/")
    public Result hello() {
        return Result.success("访问成功");
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public Result login(@RequestBody Account account) {
        if (ObjectUtil.isEmpty(account.getUsername()) || ObjectUtil.isEmpty(account.getPassword())
                || ObjectUtil.isEmpty(account.getRole())) {
            return Result.error(ResultCodeEnum.PARAM_LOST_ERROR);
        }
        if (RoleEnum.ADMIN.name().equals(account.getRole())) {
            account = adminService.login(account);
        } else if (RoleEnum.USER.name().equals(account.getRole())) {
            account = userService.login(account);
        } else {
            return Result.error(ResultCodeEnum.PARAM_ERROR);
        }
        return Result.success(account);
    }

    /**
     * 邮箱登录
     */
    @PostMapping("/loginEmail")
    public Result loginEmail(@RequestBody Validation validation) {
        String email = validation.getEmail();
        String code = validation.getCode();
        if (StrUtil.isBlank(email)||StrUtil.isBlank(code)){
            return Result.error(ResultCodeEnum.PARAM_ERROR);
        }
        User u = userService.loginEmail(validation);
        return Result.success(u);
    }

    /**
     * 重置密码
     */
    @PutMapping("/reset")
    public Result reset(@RequestBody Validation validation) {
        String email = validation.getEmail();
        String code = validation.getCode();
        if (StrUtil.isBlank(email)||StrUtil.isBlank(code)){
            return Result.error(ResultCodeEnum.PARAM_ERROR);
        } //先查询邮箱验证的表，看看之前有没有发送过邮箱code，如果不存在，就重新获取
        QueryWrapper<Validation> validationQueryWrapper=new QueryWrapper<>();
        validationQueryWrapper.eq("email",email);
        validationQueryWrapper.eq("code",code);
        validationQueryWrapper.ge("time",new Date());//查询数据库没过期的code   where time >= now
        Validation one = validationService.getOne(validationQueryWrapper);
        if (one == null){
            throw new CustomException(ResultCodeEnum.VALIDATION_EXPIRE);
        }
        User user = userMapper.selectByEmail(email); //如果验证通过了，就查询用户信息
        user.setPassword("123");     //重置密码
        userService.updateById(user);
        return Result.success();
    }

    /**
     * 发送邮箱验证码
     */
    @GetMapping("/email/{email}/{type}")
    public Result sendEmailCode(@PathVariable String email,@PathVariable Integer type){
        if (StrUtil.isBlank(email)){
            throw new CustomException(ResultCodeEnum.EMAIL_NOT_EXIST_ERROR);
        }
        if (type == null){
            throw new CustomException(ResultCodeEnum.TYPE_NOT_EXIST_ERROR);
        }
        userService.sendEmailCode(email,type);
        return Result.success();
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    public Result register(@RequestBody Account account) {
        if (StrUtil.isBlank(account.getUsername()) || StrUtil.isBlank(account.getPassword())
                || ObjectUtil.isEmpty(account.getRole())) {
            return Result.error(ResultCodeEnum.PARAM_LOST_ERROR);
        }
        if (RoleEnum.USER.name().equals(account.getRole())) {
            userService.register(account);
        } else {
            return Result.error(ResultCodeEnum.PARAM_ERROR);
        }
        return Result.success();
    }

    /**
     * 修改密码
     */
    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody Account account) {
        if (StrUtil.isBlank(account.getUsername()) || StrUtil.isBlank(account.getPassword())
                || ObjectUtil.isEmpty(account.getNewPassword())) {
            return Result.error(ResultCodeEnum.PARAM_LOST_ERROR);
        }
        if (RoleEnum.ADMIN.name().equals(account.getRole())) {
            adminService.updatePassword(account);
        }else  if (RoleEnum.USER.name().equals(account.getRole())) {
            userService.updatePassword(account);
        }
        return Result.success();
    }

}
