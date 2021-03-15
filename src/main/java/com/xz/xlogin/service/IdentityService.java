package com.xz.xlogin.service;

import com.xz.xlogin.bean.App;
import com.xz.xlogin.bean.Identity;
import com.xz.xlogin.bean.User;

/**
 * @Author: xz
 * @Date: 2021/3/9
 */
public interface IdentityService {
    /**
     * 创建token
     */
    String makeToken(App app, User user, String tPwd);

    /**
     * 校验token
     */
    Identity verifyToken(App app, User user, String token);

    /**
     * 删除token
     */
    void deleteToken(Identity identity);
}
