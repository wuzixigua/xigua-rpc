package com.xigua.xiguarpc.registry;

import com.xigua.xiguarpc.config.RegistryConfig;
import com.xigua.xiguarpc.model.ServiceMetaInfo;

import java.util.List;

/**
 * 注册中心接口   后续可以实现不同类型的注册中心
 *      提供 初始化、注册服务、销毁服务、服务发现（获取服务节点列表）、服务销毁
 */
public interface Registry {

    /**
     * 初始化
     * @param registryConfig
     */
    void init(RegistryConfig registryConfig);

    /**
     * 注册服务（服务端）
     * @param serviceMetaInfo
     * @throws Exception
     */
    void register(ServiceMetaInfo serviceMetaInfo) throws Exception;

    /**
     * 注销服务（服务端）
     * @param serviceMetaInfo
     * @throws Exception
     */
    void unRegister(ServiceMetaInfo serviceMetaInfo);

    /**
     * 服务发现（获取某服务的所有节点，消费端）
     * @param serviceKey  服务键名
     * @return
     */
    List<ServiceMetaInfo> serviceDiscovery(String serviceKey);

    /**
     * 服务销毁
     */
    void destroy();

    /**
     * 心跳检测(服务端)
     */
    void heartBeat();

    /**
     * 监听(消费端)
     * @param serviceNodeKey
     */
    void watch(String serviceNodeKey);
}

