package com.xz.xlogin.service;

import com.xz.xlogin.bean.App;
import com.xz.xlogin.bean.User;

/**
 * @Author: xz
 * @Date: 2021/3/9
 */
public interface IdentityService {
    /**
     * 创建token
     */
    String makeToken(App app, User user,String tPwd);
}
