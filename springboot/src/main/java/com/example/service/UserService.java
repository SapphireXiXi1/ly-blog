package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.common.Constants;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.common.enums.ValidationEnum;
import com.example.entity.Account;
import com.example.entity.User;
import com.example.entity.Validation;
import com.example.exception.CustomException;
import com.example.mapper.UserMapper;
import com.example.utils.RandomUtil;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class UserService{

    @Resource
    private UserMapper userMapper;

    @Autowired
    JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Resource
    private ValidationService validationService;

    public void add(User user){
        //业务方法
        //1.判断用户账号是否重复
        User dbUser = userMapper.selectByUsername(user.getUsername());
        if (dbUser!=null){
            throw new CustomException(ResultCodeEnum.USER_EXIST_ERROR);
        }
        //2.判断用户密码是不是空
        if (ObjectUtil.isEmpty(user.getPassword())){
            user.setPassword(Constants.USER_DEFAULT_PASSWORD);//默认密码 123
        }
        //3.判断用户名称是不是空
        if (ObjectUtil.isEmpty(user.getName())){
            user.setName(user.getUsername());
        }
        //4.默认用户角色
        user.setRole(RoleEnum.USER.name());
        userMapper.insert(user);
    }

    public void deleteById(Integer id) {
        userMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids){
            this.deleteById(id);
        }
    }

    public void updateById(User user) {
        userMapper.updateById(user);
    }

    public User selectById(Integer id) {
        return userMapper.selectById(id);
    }

    public List<User> selectAll(User user) {
        return userMapper.selectAll(user);
    }

    public PageInfo<User> selectPage(User user, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<User> list = userMapper.selectAll(user);
        return PageInfo.of(list);
    }

    public Account login(Account account) {
        Account dbUser = userMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbUser)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account.getPassword().equals(dbUser.getPassword())) {
            throw new CustomException(ResultCodeEnum.USER_ACCOUNT_ERROR);
        }
        // 生成token
        String tokenData = dbUser.getId() + "-" + RoleEnum.USER.name();
        String token = TokenUtils.createToken(tokenData, dbUser.getPassword());
        dbUser.setToken(token);
        return dbUser;
    }

    /**
     * 注册
     */
    public void register(Account account) {
        User user = new User();
        BeanUtils.copyProperties(account, user);
        this.add(user);
    }

    public void updatePassword(Account account) {
        User dbUser = userMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbUser)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account.getPassword().equals(dbUser.getPassword())) {
            throw new CustomException(ResultCodeEnum.PARAM_PASSWORD_ERROR);
        }
        dbUser.setPassword(account.getNewPassword());
        this.updateById(dbUser);
    }

    public User loginEmail(Validation validation) {
        Date now=new Date();
        String email = validation.getEmail();
        String code = validation.getCode();

        //先查询邮箱验证的表，看看之前有没有发送过邮箱code，如果不存在，就重新获取
        QueryWrapper<Validation> validationQueryWrapper=new QueryWrapper<>();
        validationQueryWrapper.eq("email",email);
        validationQueryWrapper.eq("code",code);
        validationQueryWrapper.ge("time",now);//查询数据库没过期的code   where time >= now
        Validation one = validationService.getOne(validationQueryWrapper);
        if (one == null){
            throw new CustomException(ResultCodeEnum.VALIDATION_EXPIRE);
        }

        //如果验证通过了，就查询用户信息
        User user = userMapper.selectByEmail(email);
        if (ObjectUtil.isNull(user)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        // 生成token
        String tokenData = user.getId() + "-" + RoleEnum.USER.name();
        String token = TokenUtils.createToken(tokenData, user.getPassword());
        user.setToken(token);

        return user;
    }

    @Transactional
    public void sendEmailCode(String email,Integer type) {
        Date now=new Date();
        //先查询同类型方法
        QueryWrapper<Validation> validationQueryWrapper=new QueryWrapper<>();
        validationQueryWrapper.eq("email",email);
        validationQueryWrapper.eq("type",type);
        validationQueryWrapper.ge("time",now);//查询数据库没过期的code
        Validation validationServiceOne = validationService.getOne(validationQueryWrapper);
        if (validationServiceOne!=null){
            throw new CustomException(ResultCodeEnum.VALIDATION_EFFECTIVE);
        }

        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom(from);//发送人
        message.setTo(email);
        message.setSentDate(now);
        String code = RandomUtil.generateAllLetterStr(4);
        if (ValidationEnum.LOGIN.getCode().equals(type)) {
            message.setSubject("【梁航】登录邮箱验证");
            message.setText("你本次登录的验证码是："+code+"，有效期5分钟，请妥善保管，切勿泄露");
        } else if (ValidationEnum.FORGET_PASS.getCode().equals(type)){
            message.setSubject("【梁航】忘记密码验证");
            message.setText("你本次忘记密码的验证码是："+code+"，有效期5分钟，请妥善保管，切勿泄露");
        }
//        message.setCc("抄送人");
//        message.setBcc("密送人");
        javaMailSender.send(message);

        //发送成功后把验证码存到数据库
        validationService.saveCode(email,code, type, DateUtil.offsetMinute(now,1));
    }
}
