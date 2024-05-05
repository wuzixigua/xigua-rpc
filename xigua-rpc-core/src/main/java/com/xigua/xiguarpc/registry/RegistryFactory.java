package com.xigua.xiguarpc.registry;

import com.xigua.xiguarpc.utils.SpiLoader;

/**
 * 注册中心工厂 （用于获取注册中心对象）
 */
public class RegistryFactory {

    //SPI动态加载
    static{
        SpiLoader.load(Registry.class);
    }

    /**
     * 默认注册中心
     */
    private static final Registry DEFAULT_REGISTRY = new EtcdRegistry();

    public static Registry getInstance(String key){
        return SpiLoader.getInstance(Registry.class,key);
    }
}
