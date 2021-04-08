package com.xz.xlogin.service.impl;

import com.xz.xlogin.bean.App;
import com.xz.xlogin.repository.AppRepo;
import com.xz.xlogin.service.AppService;
import com.xz.xlogin.utils.RedisUtil;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: xz
 * @Date: 2021/3/8
 */
@Service
public class AppServiceImpl implements AppService {
    @Autowired
    AppRepo appRepo;
    @Autowired
    RedisUtil redisUtil;

    /**
     * 验证appId合法性
     *
     * @param appId 待校验的appId
     * @return true 存在  false 不存在
     */
    @Override
    public boolean verifyByAppId(@NonNull String appId) {
        return getAppSecret(appId) != null;
    }

    @Override
    public String getAppSecret(@NonNull String appId) {
        //todo 删除appid记得也要删除缓存中的appId
        //查询缓存中的是否存在
        boolean isExist = redisUtil.hHasKey("app_auth", appId);
        if (isExist) {
            return (String) redisUtil.hget("app_auth", appId);
        }
        //走数据库
        String secret = appRepo.getAppSecret(appId);
        if (secret != null) {
            redisUtil.hset("app_auth", appId, secret);
            return secret;
        }
        return null;
    }

    @Override
    public App getApp(String appId) {
        return appRepo.findByAppId(appId);
    }

    /**
     * 验证邮箱验证码
     *
     * @return 3 验证码过期
     * 2 验证码错误
     * 1 验证成功
     */
    public int verifyEmailCode(String email, String code) {
        String rightCode = (String) redisUtil.get(email);
        if (rightCode == null) {
            return 3;
        }
        if (code.equalsIgnoreCase(rightCode)) {
            redisUtil.del(email);
            return 1;
        } else {
            return 2;
        }
    }

}

