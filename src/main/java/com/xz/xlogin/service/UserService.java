package com.xz.xlogin.service;


import com.xz.xlogin.pojo.User;
import com.xz.xlogin.pojo.vo.ApiResult;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @Author: xz
 * @Date: 2020/11/22
 */
public interface UserService {
    ApiResult register(String password, String phone);

    String login(User user, String rsaPwd,long timestamp) ;

    User findByUserPhoneOrUserNo(String userNo);

    //验证密码
    boolean validatePwd(User user, String rsaPwd,long timestamp);


    User findUserNo(String userNo);

    User findUserName(String userName);

    User findUserPhone(String phone);

    User findUserToken(String account);

    User findUUID(String uuid);

    List<User> findAll();

    Page<User> getAllUserByOnlyPage(Integer page, Integer size);

    void alterUserName(String uuid, String newUserName);

    void alterUserPwd(String uuid, String newUserPwd,long timestamp);

    void updateStateByToken(String uuid,String token);

}
