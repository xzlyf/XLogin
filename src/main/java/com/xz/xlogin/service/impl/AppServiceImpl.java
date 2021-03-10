package com.xz.xlogin.service.impl;

import com.xz.xlogin.bean.App;
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

    /**
     * 验证appId合法性
     *
     * @param appId 待校验的appId
     */
    @Override
    public App verifyByAppId(@NonNull String appId) {
        return appRepo.findByAppId(appId);
    }
}

