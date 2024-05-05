package com.xigua.example.consumer;

import com.xigua.example.common.model.User;
import com.xigua.example.common.service.UserService;
import com.xigua.xiguarpc.bootstrap.ConsumerBootstrap;
import com.xigua.xiguarpc.config.RpcConfig;
import com.xigua.xiguarpc.constant.RpcConstant;
import com.xigua.xiguarpc.proxy.ServiceProxyFactory;
import com.xigua.xiguarpc.utils.ConfigUtils;

/**
 * 服务消费者示例
 *  作为扩展RPC项目的示例消费者
 */
public class ConsumerExample {

    public static void main(String[] args) {

        //服务提供者初始化
        ConsumerBootstrap.init();

        //获取代理
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
        long number = userService.getNumber();
        System.out.println(number);
    }
}
