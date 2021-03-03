package com.xz.xlogin.repository;

import com.xz.xlogin.pojo.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @Author: xz
 * @Date: 2020/11/27
 * user_detail表  dao
 */
public interface UserDetailRepository extends JpaRepository<UserDetail, Long> {
    /**
     * 增
     */
    //UserDetail save(UserDetail detail);
    /**
     * 删
     */

    /**
     * 改
     */
    @Modifying
    @Query("update UserDetail t  set t=?2 where t.user.uuid=?1")
    int updateDetail(String uuid, UserDetail detail);

    /**
     * 查
     */

    @Query("select d from UserDetail d join d.user u  where  u.uuid=?1 ")
    UserDetail findByUUID(@Param("uuid") String uuid);
}
