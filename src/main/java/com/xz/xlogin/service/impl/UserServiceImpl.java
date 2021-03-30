package com.xz.xlogin.service.impl;

import com.xz.xlogin.bean.User;
import com.xz.xlogin.bean.entity.AccountMark;
import com.xz.xlogin.constant.Local;
import com.xz.xlogin.constant.StatusEnum;
import com.xz.xlogin.repository.UserRepo;
import com.xz.xlogin.service.UserService;
import com.xz.xlogin.utils.DESUtil;
import com.xz.xlogin.utils.RSAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;

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
    public AccountMark existCert(String account, String type) {
        AccountMark mark = new AccountMark();
        switch (type) {
            case "phone":
                if (isExistByPhone(account) != null) {
                    mark.setExist(true);
                    mark.setStatusCode(StatusEnum.STATUS_681.getCode());
                } else {
                    mark.setExist(false);
                    mark.setStatusCode(StatusEnum.STATUS_692.getCode());
                }
                break;
            case "email":
                if (isExistByEmail(account) != null) {
                    mark.setExist(true);
                    mark.setStatusCode(StatusEnum.STATUS_682.getCode());
                } else {
                    mark.setExist(false);
                    mark.setStatusCode(StatusEnum.STATUS_691.getCode());
                }
                break;
            case "qq":
                if (isExistByQQ(account) != null) {
                    mark.setExist(true);
                    mark.setStatusCode(StatusEnum.STATUS_683.getCode());
                } else {
                    mark.setExist(false);
                    mark.setStatusCode(StatusEnum.STATUS_690.getCode());
                }
                break;
            case "token":
                mark.setExist(true);
                mark.setStatusCode(StatusEnum.STATUS_606.getCode());
                break;
            default:
                mark.setExist(false);
                mark.setStatusCode(StatusEnum.STATUS_400.getCode());
                break;
        }
        return mark;
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
        String desPwd;
        //将明文密码进行单项加密存入数据库
        try {
            desPwd = DESUtil.encrypt(tPwd, Local.secretKey);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        switch (type) {
            case "account":
                return userRepo.findByUserNoAndUserPwd(cert, desPwd);
            case "phone":
                return userRepo.findByUserPhoneAndUserPwd(cert, desPwd);
            case "email":
                return userRepo.findByUserEmailAndUserPwd(cert, desPwd);
            case "qq":
                return userRepo.findByOrderQQ(tPwd);
            case "token":
                //手机号或邮箱都可登录
                User user = userRepo.findByUserPhone(cert);
                if (user == null) {
                    user = userRepo.findByUserEmail(cert);
                }
                return user;
            default:
                return null;
        }
    }

    /**
     * 重置密码
     *
     * @param cert 账号
     * @param tPwd 账号明文
     * @param type 账号类型
     */
    @Override
    public int resetPwd(String cert, String tPwd, String type) {
        String desPwd;
        //将明文密码进行单项加密存入数据库
        try {
            desPwd = DESUtil.encrypt(tPwd, Local.secretKey);
        } catch (Exception e) {
            e.printStackTrace();
            return 2;
        }
        User user = null;
        switch (type) {
            case "phone":
                user = userRepo.findByUserPhone(cert);
                break;
            case "email":
                user = userRepo.findByUserEmail(cert);
                break;
        }
        if (user == null) {
            return 3;
        }
        userRepo.updateUserPwd(user.getUuid(), desPwd, new Date(System.currentTimeMillis()));
        return 1;
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
