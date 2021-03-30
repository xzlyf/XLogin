package com.xz.xlogin.service.impl;

import com.xz.xlogin.bean.App;
import com.xz.xlogin.bean.vo.ApiResult;
import com.xz.xlogin.constant.StatusEnum;
import com.xz.xlogin.repository.AppRepo;
import com.xz.xlogin.service.AppService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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
    RedisServiceImpl redisServiceImpl;

    /**
     * 验证appId合法性
     *
     * @param appId 待校验的appId
     */
    @Override
    public App verifyByAppId(@NonNull String appId) {
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
        String rightCode = redisServiceImpl.get(email);
        if (rightCode == null) {
            return 3;
        }
        if (code.equalsIgnoreCase(rightCode)) {
            redisServiceImpl.remove(email);
            return 1;
        } else {
            return 2;
        }
    }

}

