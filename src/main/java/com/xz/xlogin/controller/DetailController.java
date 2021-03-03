package com.xz.xlogin.controller;

import com.xz.xlogin.constant.StatusEnum;
import com.xz.xlogin.pojo.User;
import com.xz.xlogin.pojo.UserDetail;
import com.xz.xlogin.pojo.vo.ApiResult;
import com.xz.xlogin.service.impl.DetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: xz
 * @Date: 2021/3/3
 */
@RestController
@RequestMapping("/userDetail")
public class DetailController {
    @Autowired
    private DetailServiceImpl detailServiceImpl;

}
