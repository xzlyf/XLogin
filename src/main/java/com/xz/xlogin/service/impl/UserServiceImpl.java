package com.xz.xlogin.service.impl;

import com.xz.xlogin.bean.User;
import com.xz.xlogin.repository.UserRepo;
import com.xz.xlogin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: xz
 * @Date: 2021/3/4
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepo userRepo;

    @Override
    public boolean register(User user) {
        try {
            userRepo.save(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String isExistByUserNo(String userNo) {
        return userRepo.isExistByUserNo(userNo);
    }

    @Override
    public String isExistByPhone(String phone) {
        return userRepo.isExistByPhone(phone);
    }

    @Override
    public String isExistByEmail(String email) {
        return userRepo.isExistByEmail(email);
    }

    @Override
    public String isExistByQQ(String qq) {
        return userRepo.isExistByQQ(qq);
    }
}
