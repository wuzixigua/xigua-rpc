package com.xigua.xiguarpc.proxy;

import java.lang.reflect.Proxy;

/**
 * 动态代理工厂
 *  根据指定类创建动态代理对象
 */
public class ServiceProxyFactory {

    public static <T> T getProxy(Class<T> serviceClass) {
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new ServiceProxy()
        );
    }
}
