package com.xigua.example.provider;

import com.xigua.example.common.model.User;
import com.xigua.example.common.service.UserService;

/**
 * 用户服务实现类
 */
public class UserServiceImpl implements UserService {
    @Override
    public User getUser(User user) {
        System.out.println("用户名："+user.getName());
        user.setName("response:"+user.getName());
        return user;
    }
}
