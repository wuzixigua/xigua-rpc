package com.xigua.example.provider;

import com.xigua.example.common.service.UserService;
import com.xigua.xiguarpc.RpcApplication;
import com.xigua.xiguarpc.bootstrap.ProviderBootstrap;
import com.xigua.xiguarpc.config.RegistryConfig;
import com.xigua.xiguarpc.config.RpcConfig;
import com.xigua.xiguarpc.model.ServiceMetaInfo;
import com.xigua.xiguarpc.model.ServiceRegisterInfo;
import com.xigua.xiguarpc.registry.LocalRegistry;
import com.xigua.xiguarpc.registry.Registry;
import com.xigua.xiguarpc.registry.RegistryFactory;
import com.xigua.xiguarpc.server.HttpServer;
import com.xigua.xiguarpc.server.VertxHttpServer;
import com.xigua.xiguarpc.server.tcp.VertxTcpServer;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务提供者示例
 *  能够根据配置动态地在不同端口启动web服务
 */
public class ProviderExample {

    public static void main(String[] args) {
        //要注册的服务
        List<ServiceRegisterInfo<?>> serviceRegisterInfoList = new ArrayList<>();
        ServiceRegisterInfo serviceRegisterInfo = new ServiceRegisterInfo(UserService.class.getName(), UserServiceImpl.class);
        serviceRegisterInfoList.add(serviceRegisterInfo);

        //服务提供者初始化
        ProviderBootstrap.init(serviceRegisterInfoList);

    }
}
