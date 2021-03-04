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
     * @param no 指定账号
     * @return 存在返回账号  不存在返回空
     */
    @Query("select u.userNo from User u where u.userNo = ?1")
    String isExistByUserNo(String no);
}
