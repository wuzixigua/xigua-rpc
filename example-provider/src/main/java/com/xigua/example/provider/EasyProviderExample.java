package com.xigua.example.provider;

import com.xigua.example.common.service.UserService;
import com.xigua.xiguarpc.registry.LocalRegistry;
import com.xigua.xiguarpc.server.HttpServer;
import com.xigua.xiguarpc.server.VertxHttpServer;

/**
 * 简易服务提供者示例
 * 服务提供者启动类，在该类的 main 方法中编写提供服务的代码
 */
public class EasyProviderExample {

    public static void main(String[] args) {
        //服务启动时需要将服务注册到本地注册器中
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);
        // 启动 web 服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(8080);
    }
}
