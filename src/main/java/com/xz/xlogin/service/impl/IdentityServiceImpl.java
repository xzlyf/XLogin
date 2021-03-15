package com.xz.xlogin.service.impl;

import com.xz.xlogin.bean.App;
import com.xz.xlogin.bean.Identity;
import com.xz.xlogin.bean.User;
import com.xz.xlogin.repository.IdentityRepo;
import com.xz.xlogin.service.IdentityService;
import com.xz.xlogin.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @Author: xz
 * @Date: 2021/3/10
 */
@Service
public class IdentityServiceImpl implements IdentityService {
    @Autowired
    IdentityRepo identityRepo;

    @Override
    public String makeToken(App app, User user, String tPwd) {
        String token;
        String appid = app.getAppId();
        long lastTime = System.currentTimeMillis();
        token = MD5Util.getMD5(appid + tPwd + lastTime);

        //如果存在更新，不存在则插入
        Identity identity = identityRepo.findByAppAndUser(app, user);
        if (identity == null) {
            //插入
            identity = new Identity();
            identity.setApp(app);
            identity.setUser(user);
            identity.setToken(token);
            identity.setLastLoginTime(new Date(lastTime));
            identityRepo.save(identity);
        } else {
            //更新
            identityRepo.updateToken(token, app, user);
        }
        return token;
    }

    @Override
    public Identity verifyToken(App app, User user, String token) {
        return identityRepo.findByAppAndUserAndToken(app, user, token);
    }

    @Modifying
    @Transactional
    @Override
    public void deleteToken(Identity identity) {
        identityRepo.delete(identity);
    }


}
