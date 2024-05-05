package com.xigua.examplespringbootprovider;

import com.xigua.example.common.model.User;
import com.xigua.example.common.service.UserService;
import com.xigua.xiguarpc.springboot.starter.annotation.RpcService;
import org.springframework.stereotype.Service;

@Service
@RpcService
public class UserServiceImpl implements UserService {
    @Override
    public User getUser(User user) {
        System.out.println("用户名：" + user.getName());
        user.setName("response: "+ user.getName());
        return user;
    }
}
