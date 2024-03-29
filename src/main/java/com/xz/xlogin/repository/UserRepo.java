package com.xz.xlogin.repository;

import com.xz.xlogin.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @Author: xz
 * @Date: 2021/3/4
 */
public interface UserRepo extends JpaRepository<User, Long> {
    @Transactional
    @Modifying
    @Query("update User t  set t.userPwd=?2,t.updateTime=?3 where t.uuid=?1")
    void updateUserPwd(String uuid, String userPwd, Date date);

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

    User findByUserNoAndUserPwd(String userNo, String userPwd);

    User findByUserPhoneAndUserPwd(String userPhone, String userPwd);

    User findByUserEmailAndUserPwd(String userEmail, String userPwd);

    User findByOrderQQ(String qqSecret);

    User findByUserPhone(String phone);

    User findByUserNo(String userNO);

    User findByUserEmail(String userEmail);


}
