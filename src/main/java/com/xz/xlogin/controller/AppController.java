package com.xz.xlogin.controller;

import com.xz.xlogin.bean.vo.ApiResult;
import com.xz.xlogin.constant.StatusEnum;
import com.xz.xlogin.service.impl.AppServiceImpl;
import com.xz.xlogin.utils.EmailUtil;
import com.xz.xlogin.utils.RandomUtil;
import com.xz.xlogin.utils.RegexUtil;
import com.xz.xlogin.utils.VerifyCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
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
    @Autowired
    private EmailUtil mailUtil;

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
        HttpSession session = request.getSession();
        System.out.println("获取图片验证码session:" + session.getId() + "==是否新的:" + session.isNew());
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
        System.out.println("验证验证码session:" + session.getId() + "==是否新的:" + session.isNew());
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

    /**
     * 发送普通邮件
     */
    @GetMapping("/sendVerifyEmail")
    public Object verifyUser(@RequestParam String email,
                             @RequestParam String key) {
        //判断邮箱合法性
        if (!RegexUtil.doRegex(email, RegexUtil.REGEX_EMAIL)) {
            return new ApiResult(StatusEnum.STATUS_308, null);
        }
        //判断key合法
        //todo 判断key合法性


        try {
            mailUtil.sendVerifyCode(RandomUtil.getRandom(4), email);
        } catch (MessagingException e) {
            e.printStackTrace();
            return new ApiResult(StatusEnum.ERROR, "验证码邮件发送异常，请检查邮箱地址");
        }

        //邮件已开始发送，必要时提示用户在垃圾邮件找回验证码
        return new ApiResult(StatusEnum.SUCCESS, null);
    }
}
