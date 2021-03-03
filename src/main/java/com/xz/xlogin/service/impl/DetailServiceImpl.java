package com.xz.xlogin.service.impl;

import com.xz.xlogin.pojo.User;
import com.xz.xlogin.pojo.UserDetail;
import com.xz.xlogin.repository.UserDetailRepository;
import com.xz.xlogin.service.DetailService;
import com.xz.xlogin.utils.MyBeanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @Author: xz
 * @Date: 2020/11/30
 */
@Service
public class DetailServiceImpl implements DetailService {
    @Autowired
    UserDetailRepository userDetailRepo;

    /**
     * 更新用户信息
     *
     * @param uuid
     * @param detail
     * @return
     */
    @Transactional
    @Override
    public int updateDetail(String uuid, UserDetail detail) {

        //防止被改id
        detail.setId(null);
        UserDetail target = userDetailRepo.findByUUID(uuid);
        if (target == null) {
            //没有找到对应的用户数据，创建新的用户信息
            target = saveDetail(uuid, detail);
        }
        BeanUtils.copyProperties(detail, target, MyBeanUtils.getNullPropertyNames(detail));
        detail.setUpdateTime(new Date());
        return userDetailRepo.updateDetail(uuid, target);
    }

    /**
     * 创建用户信息
     *
     * @param uuid
     * @param detail
     * @return
     */
    @Override
    public UserDetail saveDetail(String uuid, UserDetail detail) {
        User user = new User();
        user.setUuid(uuid);
        detail.setUser(user);
        return userDetailRepo.save(detail);
    }
}
