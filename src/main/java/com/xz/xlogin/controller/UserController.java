package com.xz.xlogin.controller;

import com.xz.xlogin.bean.App;
import com.xz.xlogin.bean.Identity;
import com.xz.xlogin.bean.User;
import com.xz.xlogin.bean.UserDetail;
import com.xz.xlogin.bean.entity.AccountMark;
import com.xz.xlogin.bean.vo.ApiResult;
import com.xz.xlogin.constant.Local;
import com.xz.xlogin.constant.StatusEnum;
import com.xz.xlogin.service.impl.AppServiceImpl;
import com.xz.xlogin.service.impl.IdentityServiceImpl;
import com.xz.xlogin.service.impl.UserServiceImpl;
import com.xz.xlogin.utils.AccountGenerate;
import com.xz.xlogin.utils.DESUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @Author: xz
 * @Date: 2021/3/4
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserServiceImpl userServiceImpl;
    @Autowired
    AppServiceImpl appServiceImpl;
    @Autowired
    IdentityServiceImpl identityServiceImpl;

    /**
     * 注册接口
     *
     * @param appId     appId
     * @param pwd       rsa加密密码
     * @param cert      注册账号
     * @param type      账号类型 phone-手机 email-邮箱 qq-qq接入
     * @param timestamp 时间戳
     * @param st        随机字符串
     * @return
     */
    @PostMapping("/register")
    public Object register(@RequestHeader(value = "appId") String appId,
                           @RequestParam(value = "pwd") String pwd,
                           @RequestParam(value = "cert") String cert,
                           @RequestParam(value = "type") String type,
                           @RequestParam(value = "t") Long timestamp,
                           @RequestParam(value = "st") String st) {

        //判断账号是否已注册
        AccountMark mark = userServiceImpl.existCert(cert, type);
        if (mark.isExist()) {
            return new ApiResult(StatusEnum.getEnum(mark.getStatusCode()), null);
        }
        //验证appId ----访问AppController的接口
        if (!appServiceImpl.verifyByAppId(appId)) {
           //appId不存在
            return new ApiResult(StatusEnum.STATUS_306, null);
        }
        //解密RSA
        String tPwd = userServiceImpl.decodeRSA(pwd);
        if (tPwd == null) {
            return new ApiResult(StatusEnum.STATUS_307, null);
        }

        //创建一个新用户对象
        User user = new User();
        switch (type) {
            case "phone":
                user.setUserPhone(cert);
                break;
            case "email":
                user.setUserEmail(cert);
                break;
            case "qq":
                user.setOrderQQ(cert);
                break;
        }
        //随机生成一个账号,如果存在则重新生成
        String userNo;
        do {
            userNo = AccountGenerate.makeAccount(8);
        } while (userServiceImpl.isExistByUserNo(userNo) != null);
        user.setUserNo(userNo);

        //将明文密码进行单项加密存入数据库
        try {
            user.setUserPwd(DESUtil.encrypt(tPwd, Local.secretKey));
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResult(StatusEnum.STATUS_400, "密码不符合规范");
        }

        //更新时间
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        //关联用户详情对象
        UserDetail detail = new UserDetail();
        //更新时间
        detail.setCreateTime(new Date());
        detail.setUpdateTime(new Date());
        user.setDetail(detail);

        boolean isOk = userServiceImpl.register(user);
        if (isOk) {
            return new ApiResult(StatusEnum.SUCCESS, userNo);
        } else {
            return new ApiResult(StatusEnum.ERROR, null, "请稍后重试");
        }
    }

    /**
     * 登录接口
     * token登录均可使用账号或手机号
     *
     * @param cert      账号
     * @param pwd       密码密文
     * @param type      账号类型 phone-手机 email-邮箱 qq-qq接入 account-账号  token-快速登录
     * @param timestamp 时间戳
     * @param st        随机字符串
     * @return
     */
    @PostMapping("/login")
    public Object login(@RequestHeader(value = "appid") String appId,
                        @RequestParam(value = "cert") String cert,
                        @RequestParam(value = "pwd") String pwd,
                        @RequestParam(value = "type") String type,
                        @RequestParam(value = "t") Long timestamp,
                        @RequestParam(value = "st") String st,
                        HttpServletResponse response,
                        HttpServletRequest request) {
        //todo 频繁请求处理

        //判断账号是否已注册
        AccountMark mark = userServiceImpl.existCert(cert, type);
        if (!mark.isExist()) {
            return new ApiResult(StatusEnum.getEnum(mark.getStatusCode()), null);
        }
        //验证appId ----访问AppController的接口
        if (!appServiceImpl.verifyByAppId(appId)) {
            //appId不存在
            return new ApiResult(StatusEnum.STATUS_306, null);
        }
        App app = appServiceImpl.getApp(appId);
        //解密RSA
        String tPwd = userServiceImpl.decodeRSA(pwd);
        if (tPwd == null) {
            return new ApiResult(StatusEnum.STATUS_307, null);
        }
        //获取账号对象
        User user = userServiceImpl.verifyByPwd(cert, tPwd, type);
        if (user == null) {
            return new ApiResult(StatusEnum.STATUS_601, null);
        }
        //如果登录类型为token
        if (type.equals("token")) {
            //token登录操作
            Identity identity = identityServiceImpl.verifyToken(app, user, tPwd);
            if (identity == null) {
                return new ApiResult(StatusEnum.STATUS_600, null);
            }
        }

        //生成新令牌
        String token = identityServiceImpl.makeToken(app, user, tPwd);
        if (token == null) {
            return new ApiResult(StatusEnum.STATUS_102, null);
        }

        return new ApiResult(StatusEnum.SUCCESS, token);
    }

    @GetMapping("/logout")
    public Object logout(@RequestHeader String appId,
                         @RequestParam String cert,
                         @RequestParam String token) {

        //验证appId ----访问AppController的接口
        if (!appServiceImpl.verifyByAppId(appId)) {
            //appId不存在
            return new ApiResult(StatusEnum.STATUS_306, null);
        }
        App app = appServiceImpl.getApp(appId);
        //获取账号对象
        User user = userServiceImpl.verifyByPwd(cert, token, "token");
        if (user == null) {
            return new ApiResult(StatusEnum.STATUS_601, null);
        }
        //验证token
        Identity identity = identityServiceImpl.verifyToken(app, user, token);
        if (identity == null) {
            return new ApiResult(StatusEnum.STATUS_600, null);
        }
        identityServiceImpl.deleteToken(identity);
        return new ApiResult(StatusEnum.SUCCESS, null);
    }

    @PostMapping("/reset")
    public Object reset(@RequestHeader(value = "appId") String appId,
                        @RequestParam(value = "pwd") String pwd,
                        @RequestParam(value = "cert") String cert,
                        @RequestParam(value = "type") String type,
                        @RequestParam(value = "code") String code,
                        @RequestParam(value = "t") Long timestamp,
                        @RequestParam(value = "st") String st) {
        //验证appId ----访问AppController的接口
        if (!appServiceImpl.verifyByAppId(appId)) {
            //appId不存在
            return new ApiResult(StatusEnum.STATUS_306, null);
        }
        //验证账号是否已注册
        AccountMark mark = userServiceImpl.existCert(cert, type);
        if (!mark.isExist()) {
            return new ApiResult(StatusEnum.getEnum(mark.getStatusCode()), null);
        }
        //验证验证码
        if (type.equals("phone")) {
            return new ApiResult(StatusEnum.STATUS_131, null);
        } else if (type.equals("email")) {
            int statue = appServiceImpl.verifyEmailCode(cert, code);
            if (statue != 1) {
                switch (statue) {
                    case 2:
                        return new ApiResult(StatusEnum.STATUS_122, null);
                    case 3:
                        return new ApiResult(StatusEnum.STATUS_121, null);
                    default:
                        return new ApiResult(StatusEnum.ERROR, null);
                }
            }
        } else {
            return new ApiResult(StatusEnum.STATUS_400, null);
        }

        //解密RSA
        String tPwd = userServiceImpl.decodeRSA(pwd);
        if (tPwd == null) {
            return new ApiResult(StatusEnum.STATUS_307, null);
        }

        int statue = userServiceImpl.resetPwd(cert, tPwd, type);
        if (statue == 1) {
            return new ApiResult(StatusEnum.SUCCESS, null);
        } else {
            return new ApiResult(StatusEnum.ERROR, "修改密码失败");
        }

    }
}
