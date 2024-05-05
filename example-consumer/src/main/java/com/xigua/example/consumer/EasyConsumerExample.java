package com.xigua.example.consumer;

import com.xigua.example.common.model.User;
import com.xigua.example.common.service.UserService;
import com.xigua.xiguarpc.proxy.ServiceProxyFactory;

/**
 * 简单服务消费者示例
 */
public class EasyConsumerExample {
    public static void main(String[] args) {
        //需要获取UserService的静态代理对象
        //UserService userService = new UserServiceProxy();

        //动态代理
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);

        User user = new User();
        user.setName("xigua");
        //调用
        User newUser = userService.getUser(user);
        if(newUser != null) {
            System.out.println(newUser.getName());
        }else {
            System.out.println("user == null");
        }
    }
}

