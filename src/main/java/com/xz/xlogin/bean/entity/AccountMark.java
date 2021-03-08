package com.xz.xlogin.bean.entity;

import lombok.Data;

/**
 * @Author: xz
 * @Date: 2021/3/8
 * 账号标识
 * 账号状态
 */
@Data
public class AccountMark {
    private boolean isExist;
    private int statusCode;
}
