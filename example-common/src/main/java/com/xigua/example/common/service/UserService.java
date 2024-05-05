package com.xigua.example.common.service;

import com.xigua.example.common.model.User;

/**
 * 用户服务
 */
public interface UserService {
    /**
     *获取用户
     */
    User getUser(User user);

    default short getNumber(){
        return 1;
    }
}
