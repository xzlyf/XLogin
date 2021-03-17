package com.xz.xlogin.controller;

import com.xz.xlogin.bean.vo.ApiResult;
import com.xz.xlogin.constant.StatusEnum;
import com.xz.xlogin.service.impl.AppServiceImpl;
import com.xz.xlogin.utils.VerifyCodeUtil;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
    public Object verifyAppId(String appId) {
        if (appId == null)
            return null;
        return new ApiResult(StatusEnum.SUCCESS, appServiceImpl.verifyByAppId(appId));
    }

    @GetMapping("/test")
    public void test(HttpServletResponse response, HttpServletRequest request) throws IOException {
        //接口转发  request.getContextPath() == /app/test
        response.sendRedirect(request.getContextPath() + "/checkAppId");
    }

    @RequestMapping("/verifyImage")
    public void createImg(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            response.setContentType("image/jpeg");//设置相应类型,告诉浏览器输出的内容为图片
            response.setHeader("Pragma", "No-cache");//设置响应头信息，告诉浏览器不要缓存此内容
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expire", 0);
            VerifyCodeUtil randomValidateCode = new VerifyCodeUtil();
            randomValidateCode.getRandcode(request, response);//输出验证码图片
        } catch (Exception e) {
            System.out.println("验证码获取异常:" + e.getMessage());
            e.printStackTrace();
        }
    }

    @GetMapping("verifyCode")
    public Object verifyCode(@RequestParam String code,
                             HttpServletRequest request,
                             HttpServletResponse response) {
        HttpSession session = request.getSession();
        //获取保存在会话中的正确验证码
        String validityCode = (String) session.getAttribute(VerifyCodeUtil.RANDOMCODEKEY);
        if (validityCode == null) {
            return new ApiResult(StatusEnum.STATUS_121, null);
        }
        //校验用户传入的验证码
        if (code.equalsIgnoreCase(validityCode)) {
            //验证成功，移除会话中的验证码
            session.removeAttribute(VerifyCodeUtil.RANDOMCODEKEY);
            return new ApiResult(StatusEnum.STATUS_123, null);
        } else {
            return new ApiResult(StatusEnum.STATUS_122, null);
        }

    }
}
