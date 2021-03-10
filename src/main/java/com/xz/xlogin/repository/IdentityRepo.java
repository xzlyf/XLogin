package com.xz.xlogin.repository;

import com.xz.xlogin.bean.App;
import com.xz.xlogin.bean.Identity;
import com.xz.xlogin.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: xz
 * @Date: 2021/3/9
 */
public interface IdentityRepo extends JpaRepository<Identity, Long> {

    Identity findByAppAndUser(App app, User user);

    @Transactional
    @Modifying
    @Query("update Identity identity set identity.token =?1 where identity.app=?2 and identity.user=?3")
    int updateToken(String token, App app, User user);
}
