package com.xigua.xiguarpc.springboot.starter.bootstrap;

import com.xigua.xiguarpc.RpcApplication;
import com.xigua.xiguarpc.config.RpcConfig;
import com.xigua.xiguarpc.server.tcp.VertxTcpServer;
import com.xigua.xiguarpc.springboot.starter.annotation.EnableRpc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Rpc框架启动
 *  在Spring框架初始化的时候获取 @EnableRpc 注解的属性，并初始化RPC框架
 *
 *  实现方案：
 *      实现ImportBeanDefinitionRegistrar接口，并在registerBeanDefinitions方法中，获取到项目的注解和注解属性
 */
@Slf4j
public class RpcInitBootstrap implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        //获取 EnableRpc 注解的属性值
        boolean needServer = (boolean) importingClassMetadata.getAnnotationAttributes(EnableRpc.class.getName())
                .get("needServer");

        //RPC 框架初始化（配置和注册中心）
        RpcApplication.init();

        //全局配置
        final RpcConfig rpcConfig = RpcApplication.getRpcConfig();

        //启动服务器
        if(needServer){
            VertxTcpServer vertxTcpServer = new VertxTcpServer();
            vertxTcpServer.doStart(rpcConfig.getServerPort());
        }else {
            log.info("不启动 server");
        }
    }
}
