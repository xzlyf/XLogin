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

    StatusEnum existAccount(String account, String type);

    boolean verifyPwd(String account, String rsaPwd, String type);

    String isExistByUserNo(String userNo);

    String isExistByPhone(String phone);

    String isExistByEmail(String email);

    String isExistByQQ(String qq);

}
