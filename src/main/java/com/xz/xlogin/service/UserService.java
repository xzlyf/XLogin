package com.xz.xlogin.service;


import com.xz.xlogin.bean.User;

/**
 * @Author: xz
 * @Date: 2020/11/22
 */
public interface UserService {

    boolean register(User user);

    String isExistByUserNo(String userNo);

    String isExistByPhone(String phone);

    String isExistByEmail(String email);

    String isExistByQQ(String qq);

}
