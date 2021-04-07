package com.xz.xlogin.service.impl;

import com.xz.xlogin.bean.App;
import com.xz.xlogin.bean.vo.ApiResult;
import com.xz.xlogin.constant.StatusEnum;
import com.xz.xlogin.repository.AppRepo;
import com.xz.xlogin.service.AppService;
import com.xz.xlogin.utils.RedisUtil;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;

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
        //todo 删除appid记得也要删除缓存中的appId
        //查询缓存
        List<Object> appIdList = redisUtil.lGet("appId", 0, -1);
        if (appIdList != null) {
            int index = appIdList.indexOf(appId);
            if (index != -1) {
                //在缓存中找到appid那就直接返回
                return true;
            }
        }
        //缓存找不到就查询数据库
        App app = appRepo.findByAppId(appId);
        if (app != null) {
            //如果appId的确存在则存入缓存
            redisUtil.lSet("appId", app.getAppId());
            return true;
        }
        //找遍了缓存和数据库都找不到那就返回false
        return false;
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

