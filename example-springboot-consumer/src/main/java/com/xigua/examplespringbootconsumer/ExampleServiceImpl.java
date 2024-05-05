package com.xigua.examplespringbootconsumer;

import com.xigua.example.common.model.User;
import com.xigua.example.common.service.UserService;
import com.xigua.xiguarpc.springboot.starter.annotation.RpcReference;
import org.springframework.stereotype.Service;

@Service
public class ExampleServiceImpl {

    @RpcReference
    private UserService userService;

    public void test(){
        User user = new User();
        user.setName("xigua");
        User resultUser = userService.getUser(user);
        System.out.println(resultUser.getName());
    }
}
