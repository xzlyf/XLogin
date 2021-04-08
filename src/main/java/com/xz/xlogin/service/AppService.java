package com.xz.xlogin.service;

import com.xz.xlogin.bean.App;

/**
 * @Author: xz
 * @Date: 2021/3/8
 */
public interface AppService {
    /**
     * 校验 AppId是否合法
     *
     * @param appId 待校验的appId
     * @return true  合法  False 不存在
     */
    boolean verifyByAppId(String appId);

    /**
     * 根据获取AppSecret
     *
     * @return appSecret
     */
    String getAppSecret(String appId);

    App getApp(String appId);

    int verifyEmailCode(String email, String code);
}
