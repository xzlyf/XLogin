package com.xz.xlogin.controller;

import com.xz.xlogin.bean.vo.ApiResult;
import com.xz.xlogin.constant.StatusEnum;
import com.xz.xlogin.service.RedisService;
import com.xz.xlogin.service.impl.AppServiceImpl;
import com.xz.xlogin.service.impl.UserServiceImpl;
import com.xz.xlogin.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

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
    UserServiceImpl userServiceImpl;
    @Autowired
    private EmailUtil mailUtil;
    @Autowired
    RedisService redisService;
    public static final String EMAIL_CODE_KEY = "ehtpTfxdnx9dzjcY";

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

    @GetMapping("/now")
    public Object getNow() {
        return new ApiResult(StatusEnum.SUCCESS, System.currentTimeMillis());
    }

    @RequestMapping("/verifyImage")
    public void createImg(HttpServletRequest request, HttpServletResponse response) {
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
                             HttpServletResponse response) throws IOException {
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
            //接口转发
            //response.sendRedirect(request.getContextPath() + "/sendVerifyEmail");
            //演唱session存活时间
            session.setMaxInactiveInterval(60);
            //存储redis
            redisService.set(session.getId(),
                    TimeUtil.getSimMilliDate("yyyy-MM-dd HH:mm:ss", System.currentTimeMillis()));
            redisService.expire(session.getId(), 60);
            return new ApiResult(StatusEnum.STATUS_123, null);
        } else {
            return new ApiResult(StatusEnum.STATUS_122, null);
        }

    }

    /**
     * 发送验证码邮件
     */
    @PostMapping("/sendVerifyEmail")
    public Object sendVerifyEmail(@RequestParam String email,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {
        //判断会话是否合法
        HttpSession session = request.getSession();
        //如果redis没有这个session记录也表示会话过期
        String date = redisService.get(session.getId());
        if (session.isNew() || date == null) {
            return new ApiResult(StatusEnum.STATUS_402, null);
        }
        //判断邮箱合法性
        email = URLDecoder.decode(email, StandardCharsets.UTF_8);//解决@变成%40
        if (!RegexUtil.doRegex(email, RegexUtil.REGEX_EMAIL)) {
            return new ApiResult(StatusEnum.STATUS_130, null);
        }
        //判断邮箱是否已注册
        String isExist = userServiceImpl.isExistByEmail(email);
        if (isExist == null) {
            //不存在
            return new ApiResult(StatusEnum.STATUS_691, null);
        }

        //剩余存活时间
        long expire = redisService.getExpire(email);
        if (expire >= 240) {
            //不需要重发验证码，一分钟还没到
            return new ApiResult(StatusEnum.STATUS_308, null);
        }
        //验证码
        String code = RandomUtil.getRandom(4);
        //移除会话
        redisService.remove(session.getId());
        redisService.remove(email);
        //把邮箱验证码存放进redis
        redisService.set(email, code);
        //验证码有效期5分钟，1分钟后可重发
        redisService.expire(email, 300);

        //开始发送验证码
        //try {
        //    mailUtil.sendVerifyCode(code, email);
        //} catch (MessagingException e) {
        //    e.printStackTrace();
        //    return new ApiResult(StatusEnum.ERROR, "验证码邮件发送异常，请检查邮箱地址");
        //}

        //邮件已开始发送，必要时提示用户在垃圾邮件找回验证码
        return new ApiResult(StatusEnum.SUCCESS, null);
    }

    /**
     * 验证邮箱验证码
     */
    @PostMapping("/verifyEmailCode")
    public Object verifyEmailCode(@RequestParam String email,
                                  @RequestParam String code) {
        //判断邮箱合法性
        if (!RegexUtil.doRegex(email, RegexUtil.REGEX_EMAIL)) {
            return new ApiResult(StatusEnum.STATUS_130, null);
        }

        String rightCode = redisService.get(email);
        if (rightCode == null) {
            return new ApiResult(StatusEnum.STATUS_121, null);
        }

        if (code.equalsIgnoreCase(rightCode)) {
            redisService.remove(email);
            return new ApiResult(StatusEnum.SUCCESS, null);
        } else {
            return new ApiResult(StatusEnum.STATUS_122, null);
        }
    }
}
