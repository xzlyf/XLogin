package com.xz.xlogin.service;


import com.xz.xlogin.bean.User;

/**
 * @Author: xz
 * @Date: 2020/11/22
 */
public interface UserService {

    boolean register(User user);

    String isExistByUserNo(String userNo);

}
