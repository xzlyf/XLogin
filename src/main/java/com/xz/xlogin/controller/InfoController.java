package com.xz.xlogin.controller;

import com.xz.xlogin.bean.App;
import com.xz.xlogin.bean.vo.ApiResult;
import com.xz.xlogin.constant.StatusEnum;
import com.xz.xlogin.service.impl.AppServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author: xz
 * @Date: 2021/3/15
 */
@Controller
@RequestMapping("/info")
public class InfoController {
    @Autowired
    AppServiceImpl appServiceImpl;


    @GetMapping("/getUserRules")
    public String getUserRules(@RequestParam String appId) {
        App app = appServiceImpl.verifyByAppId(appId);
        if (app == null) {
            return "404";
        }

        return privacy();
    }

    @GetMapping("/privacy")
    public String privacy() {
        return "privacy";
    }
}
