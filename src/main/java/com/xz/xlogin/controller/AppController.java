package com.xz.xlogin.controller;

import com.xz.xlogin.bean.vo.ApiResult;
import com.xz.xlogin.constant.StatusEnum;
import com.xz.xlogin.service.impl.AppServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: xz
 * @Date: 2021/3/8
 */
@RestController
@RequestMapping("/app")
public class AppController {
    @Autowired
    AppServiceImpl appServiceImpl;

    @GetMapping("/checkAppId")
    public Object verifyAppId(HttpServletRequest request) {
        String appId = request.getParameter("appId");
        if (appId == null)
            return null;
        return new ApiResult(StatusEnum.SUCCESS, appServiceImpl.verifyByAppId(appId));
    }

    @GetMapping("/test")
    public void test(HttpServletResponse response, HttpServletRequest request) throws IOException {
        //接口转发  request.getContextPath() == /app/test
        response.sendRedirect(request.getContextPath() + "/checkAppId");
    }
}
