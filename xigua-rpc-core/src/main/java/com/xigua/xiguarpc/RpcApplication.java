package com.xigua.xiguarpc;

import com.xigua.xiguarpc.config.RegistryConfig;
import com.xigua.xiguarpc.config.RpcConfig;
import com.xigua.xiguarpc.constant.RpcConstant;
import com.xigua.xiguarpc.registry.Registry;
import com.xigua.xiguarpc.registry.RegistryFactory;
import com.xigua.xiguarpc.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * Rpc 框架应用   维护全局的配置对象
 * 相当于holder ，存放了项目全局用到的变量。双检锁单例模式实现
 *
 *      双检锁单例模式的经典实现，支持在获取配置时，才调用init方法实现懒加载
 */
@Slf4j
public class RpcApplication {

    private static volatile RpcConfig rpcConfig;

    /**
     * 框架初始化，支持传入自定义配置
     *
     * @param newRpcConfig
     */
    public static void init(RpcConfig newRpcConfig){
        rpcConfig = newRpcConfig;
        log.info("rpc init, config = {}",newRpcConfig.toString());

        //注册中心初始化
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        registry.init(registryConfig);
        log.info("registry init, config = {}",registryConfig);

        //创建并注册 Shutdown Hook, JVM 退出时执行操作
        Runtime.getRuntime().addShutdownHook(new Thread(registry::destroy));
    }

    /**
     * 初始化
     */
    public static void init(){
        RpcConfig newRpcConfig;
        try {
            newRpcConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        } catch (Exception e) {
            //配置加载失败，使用默认配置
            newRpcConfig = new RpcConfig();
        }
        init(newRpcConfig);
    }

    public static RpcConfig getRpcConfig(){
        //第一次检测
        if (rpcConfig == null) {
            //锁
            synchronized (RpcApplication.class) {
                //第二次检测
                if (rpcConfig == null) {
                    init();
                }
            }
        }
        return rpcConfig;
    }
}
