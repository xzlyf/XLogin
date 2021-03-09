package com.xz.xlogin.controller;

import com.xz.xlogin.bean.User;
import com.xz.xlogin.bean.UserDetail;
import com.xz.xlogin.bean.entity.AccountMark;
import com.xz.xlogin.bean.vo.ApiResult;
import com.xz.xlogin.constant.Local;
import com.xz.xlogin.constant.StatusEnum;
import com.xz.xlogin.repository.AppRepo;
import com.xz.xlogin.service.impl.AppServiceImpl;
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
    //AppController appController;

    /**
     * 注册接口
     *
     * @param pwd       rsa加密密码
     * @param cert      注册账号
     * @param type      账号类型 phone-手机 email-邮箱 qq-qq接入
     * @param timestamp 时间戳
     * @param st        随机字符串
     * @return
     */
    @PostMapping("/register")
    public Object register(@RequestParam(value = "pwd") String pwd,
                           @RequestParam(value = "cert") String cert,
                           @RequestParam(value = "type") String type,
                           @RequestParam(value = "t") Long timestamp,
                           @RequestParam(value = "st") String st) {

        //判断账号是否已注册
        AccountMark mark = userServiceImpl.existCert(cert, type);
        if (mark.isExist()) {
            return new ApiResult(StatusEnum.getEnum(mark.getStatusCode()), null);
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
     *
     * @param cert      账号
     * @param pwd       密码密文
     * @param type      账号类型 phone-手机 email-邮箱 qq-qq接入 account-账号
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

        //判断账号是否已注册
        AccountMark mark = userServiceImpl.existCert(cert, type);
        if (!mark.isExist()) {
            return new ApiResult(StatusEnum.getEnum(mark.getStatusCode()), null);
        }
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

        //验证appId ----访问AppController的接口
        boolean isOk = appServiceImpl.verifyByAppId(appId);
        System.out.println("==================");
        System.out.println(isOk);
        if (!isOk) {
            return new ApiResult(StatusEnum.STATUS_306, null);
        }



        return null;
    }

}
