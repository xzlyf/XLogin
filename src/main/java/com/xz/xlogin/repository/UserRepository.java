package com.xz.xlogin.repository;

import com.xz.xlogin.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * user 表操作
 */
public interface UserRepository extends JpaRepository<User, Long> {


    /**
     * ========增=========
     */
    //插入用户
    User save(User user);
    //插入用户详情信息


    /**
     * ========删=========
     */


    /**
     * ========改=========
     */
    //更新表数据 by username
    @Modifying
    @Query("update User t  set t.userName=?2,t.updateTime=?3 where t.uuid=?1")
    void updateStateByUserName(String uuid, String newUserName, Date date);


    //更新表数据 by userPwd
    @Modifying
    @Query("update User t  set t.userPwd=?2,t.updateTime=?3 where t.uuid=?1")
    void updateStateByUserPwd(String uuid, String userPwd, Date date);

    //更新表数据 by token
    @Modifying
    @Query("update User t set t.token=?2 where t.uuid=?1")
    void updateStateByToken(String uuid, String token);


    /**
     * ========查=========
     */
    @Query("select t from User t where t.userNo=?1 or t.userPhone=?1")
    User findByUserPhoneOrUserNo(String account);

    //用户账号查询
    //相当于    @Query(value = "select t from UserDo t where t.userNo = ?1")
    User findByUserNo(String userNo);

    //用户名查询
    User findByUserName(String userName);

    //手机号查询
    User findByUserPhone(String userPhone);

    //token查询
    User findByToken(String token);

    //uuid查询
    User findByUuid(String uuid);

    //账号登录
    @Query("select  t from User t where t.userNo=?1 and t.userPwd=?2")
    User loginByUserNo(String userNo, String userPwd);

    //手机号登录登录 //自定义sql查询
    @Query("select  t.token from User t where t.userPhone=?1 and t.userPwd=?2")
    String loginByPhone(String userPhone, String userPwd);

    //使用token登录
    @Query("select  t.token from User t where t.userPhone=?1 and t.token=?2")
    User loginByToken(String userPhone, String token);

    //查询表中所有用户
    List<User> findAll();


}
