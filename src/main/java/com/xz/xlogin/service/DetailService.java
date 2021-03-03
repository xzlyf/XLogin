package com.xz.xlogin.service;


import com.xz.xlogin.pojo.UserDetail;

/**
 * @Author: xz
 * @Date: 2020/11/30
 */
public interface DetailService {
    int updateDetail(String uuid, UserDetail detail);

    UserDetail saveDetail(String uuid, UserDetail detail);
}
