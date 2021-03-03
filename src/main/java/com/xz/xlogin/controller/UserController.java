package com.xz.xlogin.controller;
import com.xz.xlogin.constant.Local;
import com.xz.xlogin.constant.StatusEnum;
import com.xz.xlogin.pojo.User;
import com.xz.xlogin.pojo.UserDetail;
import com.xz.xlogin.pojo.vo.ApiResult;
import com.xz.xlogin.pojo.vo.PagingResult;
import com.xz.xlogin.service.impl.DetailServiceImpl;
import com.xz.xlogin.service.impl.UserServiceImpl;
import com.xz.xlogin.utils.RSAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * user 控制层
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private DetailServiceImpl detailServiceImpl;

    /**
     * 获取当前服务器时间
     *
     * @param user
     * @return
     */
    @RequestMapping("/now")
    public Object getNow(String user) {
        if (user == null) {
            return new ApiResult(StatusEnum.SUCCESS, System.currentTimeMillis());
        } else {
            return new ApiResult(StatusEnum.SUCCESS, System.currentTimeMillis(), "Hello:" + user);
        }
    }

    /**
     * 根据账号查询用户信息
     *
     * @param userNo
     * @return
     */
    @RequestMapping("/")
    public Object getUser(@RequestParam("userno") String userNo) {
        User user = userServiceImpl.findUserNo(userNo);
        if (user == null) {
            return new ApiResult(StatusEnum.NULL_USER, null);
        } else {
            return new ApiResult(StatusEnum.SUCCESS, user);
        }
    }

    /**
     * 以用户名查询是否存在用户
     *
     * @param userName
     * @return
     */
    @RequestMapping(value = "/", params = {"username"})
    public Object checkUserName(@RequestParam(value = "username") String userName) {
        User user = userServiceImpl.findUserName(userName);
        if (user == null) {
            return new ApiResult(StatusEnum.NULL_USER, null);
        } else {
            return new ApiResult(StatusEnum.SUCCESS, user);
        }
    }

    /**
     * 获取表所有用户数据
     *
     * @return
     */
    @RequestMapping("/getAllUser")
    public Object getAllUser(Integer page, Integer size) {
        if (page == null || size == null) {
            return new ApiResult(StatusEnum.SUCCESS, userServiceImpl.findAll());
        } else {
            return new PagingResult<>(StatusEnum.SUCCESS, userServiceImpl.getAllUserByOnlyPage(page, size));
        }
    }


    /**
     * 注册用户
     * 手机注册
     *
     * @param password
     * @param phone
     * @return
     */
    @RequestMapping(value = "/registerUser", params = {"password", "phone"})
    public Object registerUser(@RequestParam(value = "password") String password,
                               @RequestParam(value = "phone") String phone) {
        return userServiceImpl.register(password, phone);
    }

    /**
     * 用户登录
     *
     * @param type 1-手机登录  2-账号登录 3-token登录
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Object login(@RequestHeader(value = "timestamp") Long timestamp,
                        @RequestParam(value = "account") String account,
                        @RequestParam(value = "password") String password,
                        @RequestParam(value = "type") Integer type) {

        User user = null;
        switch (type) {
            case 1:
                //手机号登录
                user = userServiceImpl.findUserPhone(account);
                if (user == null) {
                    return new ApiResult(StatusEnum.FAILED_USER_LOGIN_NO_USER_PHONE, null);
                }
                break;
            case 2:
                //账号登录
                user = userServiceImpl.findUserNo(account);
                if (user == null) {
                    return new ApiResult(StatusEnum.FAILED_USER_LOGIN_NO_USER_NO, null);
                }
                break;
            case 3:
                //token登录 手机号+token
                user = userServiceImpl.findUserPhone(account);
                if (user == null) {
                    return new ApiResult(StatusEnum.FAILED_USER_LOGIN_NO_USER_NO, null);
                }
                //使用私钥解密密文
                String rsToken = null;
                try {
                    rsToken = RSAUtil.privateDecrypt(password, RSAUtil.getPrivateKey(Local.privateKey));
                } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                    e.printStackTrace();
                    return new ApiResult(StatusEnum.ERROR, null);
                }
                //解密后的token
                String finalToken = rsToken.replaceAll(String.valueOf(timestamp), "");
                if (!finalToken.equalsIgnoreCase(user.getToken())) {
                    //token过期
                    return new ApiResult(StatusEnum.ERROR_TOKEN, null);
                } else {
                    //未过期  使用token登录不生成新的token
                    return new ApiResult(StatusEnum.SUCCESS, finalToken);
                }
            default:
                //参数错误
                return new ApiResult(StatusEnum.ERROR_PARAMS, null);
        }

        //long clientTimestamp;
        //try {
        //    clientTimestamp = Long.parseLong(request.getHeader("timestamp"));
        //} catch (Exception e) {
        //    e.printStackTrace();
        //    return new ApiResult(StatusEnum.ERROR_TIMESTAMP_RECEIVE, null);
        //}
        try {
            String newToken = userServiceImpl.login(user, password, timestamp);
            if (newToken == null) {
                //密码不正确
                return new ApiResult(StatusEnum.FAILED_USER_LOGIN, null);
            } else {
                //返回新的token
                return new ApiResult(StatusEnum.SUCCESS, newToken);
            }
        } catch (Exception e) {
            return new ApiResult(StatusEnum.ERROR, e.getMessage());
        }
    }

    /**
     * 注销登录
     *
     * @param userNo
     * @return
     */
    @GetMapping(value = "/logout")
    public Object logout(@RequestParam(value = "userNo") String userNo,
                         @RequestParam(value = "token") String token) {
        User user;
        try {
            //账号登录
            user = userServiceImpl.findByUserPhoneOrUserNo(userNo);
            if (user == null) {
                return new ApiResult(StatusEnum.FAILED_USER_LOGIN_NO_USER_NO, null);
            }
            if (!token.equals(user.getToken())) {
                return new ApiResult(StatusEnum.ERROR_TOKEN, null);
            }
            userServiceImpl.updateStateByToken(user.getUuid(), null);
            return new ApiResult(StatusEnum.SUCCESS, null);
        } catch (Exception e) {
            return new ApiResult(StatusEnum.ERROR, e.getMessage());
        }
    }

    /**
     * 更新用户信息
     *
     * @param detail 接收user detail 实体json数据
     * @return
     */
    @PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
    public Object updateDetail(@RequestBody UserDetail detail,
                               @RequestParam String token) {

        User user = userServiceImpl.findUserToken(token);
        if (user == null) {
            return new ApiResult(StatusEnum.ERROR_TOKEN, null);
        }
        try {
            detailServiceImpl.updateDetail(user.getUuid(), detail);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResult(StatusEnum.FAILED_USER_DETAIL_UPDATE, null);
        }
        return new ApiResult(StatusEnum.SUCCESS, null);
    }

    /**
     * 修改用户密码
     */
    @PostMapping(value = "/alterPwd")
    public Object alterUserPwd(@RequestHeader(value = "timestamp") Long timestamp,
                               @RequestParam(value = "token") String token,
                               @RequestParam(value = "pwd") String pwd,
                               @RequestParam(value = "oldPwd") String oldPwd) {
        User user = userServiceImpl.findUserToken(token);
        if (user == null) {
            return new ApiResult(StatusEnum.ERROR_TOKEN, null);
        }
        try {
            //验证旧密码
            if (userServiceImpl.validatePwd(user, oldPwd, timestamp)) {
                //修改密码
                userServiceImpl.alterUserPwd(user.getUuid(), pwd, timestamp);
                return new ApiResult(StatusEnum.SUCCESS, null);
            } else {
                return new ApiResult(StatusEnum.FAILED_USER_OLDPWD, null);
            }
        } catch (Exception e) {
            return new ApiResult(StatusEnum.FAILED_USER_UPDATE, e.getMessage());
        }
    }

    /**
     * 修改用户名
     */
    @GetMapping(value = "/alterName")
    public Object alterUserName(@RequestParam(value = "token") String token,
                                @RequestParam(value = "name") String newUserName) {
        User user = userServiceImpl.findUserToken(token);
        if (user == null) {
            return new ApiResult(StatusEnum.ERROR_TOKEN, null);
        }

        try {
            userServiceImpl.alterUserName(user.getUuid(), newUserName);
            return new ApiResult(StatusEnum.SUCCESS, null);
        } catch (Exception e) {
            return new ApiResult(StatusEnum.FAILED_USER_UPDATE, e.getMessage());
        }
    }

}
