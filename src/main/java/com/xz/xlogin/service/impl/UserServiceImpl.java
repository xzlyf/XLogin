package com.xz.xlogin.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xz.xlogin.bean.App;
import com.xz.xlogin.bean.User;
import com.xz.xlogin.constant.Local;
import com.xz.xlogin.constant.StatusEnum;
import com.xz.xlogin.repository.UserRepo;
import com.xz.xlogin.service.UserService;
import com.xz.xlogin.utils.RSAUtil;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * @Author: xz
 * @Date: 2021/3/4
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepo userRepo;

    /**
     * 注册
     *
     * @param user
     * @return
     */
    @Override
    public boolean register(User user) {
        try {
            userRepo.save(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 解密RSA密文密码
     *
     * @param rsaPwd
     * @return
     */
    @Override
    public String decodeRSA(String rsaPwd) {
        String tPwd;
        try {
            tPwd = RSAUtil.privateDecrypt(rsaPwd, RSAUtil.getPrivateKey(Local.privateKey));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return null;
        }
        return tPwd;
    }

    /**
     * 账号是否已注册
     *
     * @param account 账号
     * @param type    账号类型
     * @return 不等于空表示该账号类型存在  等于空是未注册
     */
    @Override
    public StatusEnum existCert(String account, String type) {
        switch (type) {
            case "phone":
                if (isExistByPhone(account) != null) {
                    return StatusEnum.FAILED_PHONE_EXIST;
                }
                break;
            case "email":
                if (isExistByEmail(account) != null) {
                    return StatusEnum.FAILED_EMAIL_EXIST;
                }
                break;
            case "qq":
                if (isExistByQQ(account) != null) {
                    return StatusEnum.FAILED_QQ_EXIST;
                }
                break;
            default:
                return StatusEnum.ERROR_PARAMS;
        }
        return null;
    }

    /**
     * 校验密码
     *
     * @param cert 账号
     * @param tPwd 密码密文
     * @param type 登录类型
     * @return
     */
    @Override
    public User verifyByPwd(String cert, String tPwd, String type) {
        switch (type) {
            case "account":
                return userRepo.findByUserNoAndUserPwd(cert, tPwd);
            case "phone":
                return userRepo.findByUserPhoneAndUserPwd(cert, tPwd);
            case "email":
                return userRepo.findByUserEmailAndUserPwd(cert, tPwd);
            case "qq":
                return userRepo.findByOrderQQ(tPwd);
            default:
                return null;
        }
    }

    /**
     * 验证appId合法性
     *
     * @param appId 待验证appId
     * @return
     */
    @Override
    public boolean verifyByAppId(@NonNull String appId) {

        RestTemplate restTemplate = new RestTemplate();
        String url = "localhost:8080/app/checkAppId?id=" + appId;
        String response = restTemplate.getForObject(url, String.class);
        return JSON.parseObject(response).getBoolean("data");
    }

    /**
     * 账号是否存在
     *
     * @param userNo 账号
     * @return 存在返回返回 ，不存在返回空
     */
    @Override
    public String isExistByUserNo(String userNo) {
        return userRepo.isExistByUserNo(userNo);
    }

    @Override
    public String isExistByPhone(String phone) {
        return userRepo.isExistByPhone(phone);
    }

    @Override
    public String isExistByEmail(String email) {
        return userRepo.isExistByEmail(email);
    }

    @Override
    public String isExistByQQ(String qq) {
        return userRepo.isExistByQQ(qq);
    }
}
