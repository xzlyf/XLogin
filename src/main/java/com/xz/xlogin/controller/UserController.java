package com.xz.xlogin.controller;

import com.xz.xlogin.bean.User;
import com.xz.xlogin.bean.UserDetail;
import com.xz.xlogin.bean.vo.ApiResult;
import com.xz.xlogin.constant.Local;
import com.xz.xlogin.constant.StatusEnum;
import com.xz.xlogin.service.impl.UserServiceImpl;
import com.xz.xlogin.utils.AccountGenerate;
import com.xz.xlogin.utils.DESUtil;
import com.xz.xlogin.utils.RSAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;

/**
 * @Author: xz
 * @Date: 2021/3/4
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserServiceImpl userServiceImpl;

    /**
     * 登录
     *
     * @param pwd       rsa加密密码
     * @param cert      注册账号
     * @param type      账号类型 phone-手机 email-邮箱 qq-qq接入
     * @param timestamp 时间戳
     * @param st        随机字符串
     * @return
     */
    @PostMapping("/register")
    public Object register(@RequestParam(value = "pwd") String pwd,
                           @RequestParam(value = "cert") String cert,
                           @RequestParam(value = "type") String type,
                           @RequestParam(value = "t") Long timestamp,
                           @RequestParam(value = "st") String st) {

        //TODO token 分开 多应用独立token

        //解密RSA
        String rsaPwd;
        try {
            rsaPwd = RSAUtil.privateDecrypt(pwd, RSAUtil.getPrivateKey(Local.privateKey));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return new ApiResult(StatusEnum.ERROR_SECRET, null);
        }

        //创建一个新用户对象
        User user = new User();
        switch (type) {
            case "phone":
                if (userServiceImpl.isExistByPhone(cert) != null) {
                    return new ApiResult(StatusEnum.FAILED_PHONE_EXIST, null);
                }
                user.setUserPhone(cert);
                break;
            case "email":
                if (userServiceImpl.isExistByEmail(cert) != null) {
                    return new ApiResult(StatusEnum.FAILED_EMAIL_EXIST, null);
                }
                user.setUserEmail(cert);
                break;
            case "qq":
                if (userServiceImpl.isExistByQQ(cert) != null) {
                    return new ApiResult(StatusEnum.FAILED_QQ_EXIST, null);
                }
                user.setOrderQQ(cert);
                break;
            default:
                return new ApiResult(StatusEnum.ERROR_PARAMS, null);
        }
        //随机生成一个账号,如果存在则重新生成
        String userNo;
        do {
            userNo = AccountGenerate.makeAccount(8);
        } while (userServiceImpl.isExistByUserNo(userNo) != null);
        user.setUserNo(userNo);

        //将明文密码进行单项加密存入数据库
        try {
            user.setUserPwd(DESUtil.encrypt(rsaPwd, Local.secretKey));
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResult(StatusEnum.ERROR_PARAMS, "密码不符合规范");
        }

        //更新时间
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        //创建一个用户详情对象
        UserDetail detail = new UserDetail();
        //更新时间
        detail.setCreateTime(new Date());
        detail.setUpdateTime(new Date());
        user.setDetail(detail);

        boolean isOk = userServiceImpl.register(user);
        if (isOk) {
            return new ApiResult(StatusEnum.SUCCESS, userNo);
        } else {
            return new ApiResult(StatusEnum.ERROR, null, "请稍后重试");
        }
    }

}
