package com.xz.xlogin.service.impl;

import com.xz.xlogin.bean.App;
import com.xz.xlogin.repository.AppRepo;
import com.xz.xlogin.service.AppService;
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

    @Override
    public boolean verifyByAppId(@NonNull String appId) {
        App app = appRepo.findByAppId(appId);
        return app != null;
    }
}

