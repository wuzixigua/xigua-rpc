package com.xigua.xiguarpc.springboot.starter.bootstrap;

import com.xigua.xiguarpc.RpcApplication;
import com.xigua.xiguarpc.config.RegistryConfig;
import com.xigua.xiguarpc.config.RpcConfig;
import com.xigua.xiguarpc.model.ServiceMetaInfo;
import com.xigua.xiguarpc.registry.LocalRegistry;
import com.xigua.xiguarpc.registry.Registry;
import com.xigua.xiguarpc.registry.RegistryFactory;
import com.xigua.xiguarpc.springboot.starter.annotation.RpcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * Rpc服务提供者启动
 *  获取到所有包含@RpcService注解的类，并且通过注解的属性和反射机制，获取到要注册的服务信息，并且完成注册
 *
 *  实现方案：
 *      利用spring的特性监听Bean的加载
 *
 *      实现BeanPostProcessor接口的postProcessorAfterInitialization方法，
 *      就可以在某个服务提供者Bean初始化后，执行注册服务等操作了
 */
@Slf4j
public class RpcProviderBootstrap implements BeanPostProcessor {

    /**
     * Bean 初始化后执行，注册服务
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        Class<?> beanClass = bean.getClass();
        RpcService rpcService = beanClass.getAnnotation(RpcService.class);
        if (rpcService != null) {
            //需要注册服务
            //1.获取服务基本信息
            Class<?> interfaceClass = rpcService.interfaceClass();
            //默认值处理
            if(interfaceClass == void.class){
                interfaceClass = beanClass.getInterfaces()[0];
            }
            String serviceName = interfaceClass.getName();
            String serviceVersion = rpcService.serviceVersion();

            //2.注册服务
            //本地注册
            LocalRegistry.register(serviceName,beanClass);

            //全局配置
            final RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            //注册服务到注册中心
            RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
            Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(serviceVersion);
            serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
            serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
            try {
                registry.register(serviceMetaInfo);
            } catch (Exception e) {
                throw new RuntimeException(serviceName+ " 服务注册失败",e);
            }
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean,beanName);
    }
}
