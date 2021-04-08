package com.xz.xlogin.repository;

import com.xz.xlogin.bean.App;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @Author: xz
 * @Date: 2021/3/4
 */
public interface AppRepo extends JpaRepository<App, Long> {
    App findByAppId(String appId);

    /**
     * 查询账号是否存在
     */
    @Query("select app.appSecret from App app where app.appId = ?1")
    String getAppSecret(String appId);
}
