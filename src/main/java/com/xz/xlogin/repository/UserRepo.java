package com.xz.xlogin.repository;

import com.xz.xlogin.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @Author: xz
 * @Date: 2021/3/4
 */
public interface UserRepo extends JpaRepository<User, Long> {
    /**
     * 查询账号是否存在
     */
    @Query("select u.userNo from User u where u.userNo = ?1")
    String isExistByUserNo(String no);

    /**
     * 查询手机号是否存在
     */
    @Query("select u.userPhone from User u where u.userPhone = ?1")
    String isExistByPhone(String phone);

    /**
     * 查询邮箱是否存在于数据库
     */
    @Query("select u.userEmail from User u where u.userEmail = ?1")
    String isExistByEmail(String email);

    /**
     * 查询qq是否已绑定
     */
    @Query("select u.orderQQ from User u where u.orderQQ = ?1")
    String isExistByQQ(String qq);
}
