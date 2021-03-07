package com.xz.xlogin.service;


import com.xz.xlogin.bean.User;
import com.xz.xlogin.constant.StatusEnum;

/**
 * @Author: xz
 * @Date: 2020/11/22
 */
public interface UserService {

    boolean register(User user);

    String decodeRSA(String rsaPwd);

    StatusEnum existCert(String account, String type);

    User verifyPwd(String account, String tPwd, String type);

    String isExistByUserNo(String userNo);

    String isExistByPhone(String phone);

    String isExistByEmail(String email);

    String isExistByQQ(String qq);

}
