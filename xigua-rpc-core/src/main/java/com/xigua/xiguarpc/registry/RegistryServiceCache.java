package com.xigua.xiguarpc.registry;

import com.xigua.xiguarpc.model.ServiceMetaInfo;

import java.util.List;

/**
 * 注册中心本地缓存（消费端服务缓存）
 *  服务节点信息列表的更新频率不高，所以在服务消费者从
 *  注册中心获取到服务节点信息列表后，完全可以缓存在本地
 */
public class RegistryServiceCache {

    /**
     * 服务缓存
     */
    List<ServiceMetaInfo> serviceCache;

    /**
     * 写缓存
     * @param newServiceCache
     */
    void writeCache(List<ServiceMetaInfo> newServiceCache) {
        this.serviceCache = newServiceCache;
    }

    /**
     * 读缓存
     * @return
     */
    List<ServiceMetaInfo> readCache() {
        return this.serviceCache;
    }

    /**
     * 清空缓存
     */
    void clearCache() {
        this.serviceCache = null;
    }
}
