package com.xigua.xiguarpc.config;

import lombok.Data;

/**
 * 注册中心的配置类
 *  让用户配置连接注册中心所需的信息，
 *  比如注册中心类别、注册中心地址、用户名、密码、连接超时时间等
 *
 */
@Data
public class RegistryConfig {
    /**
     * 注册中心类别
     */
    private String registry = "etcd";
    /**
     * 注册中心地址
     */
    private String address = "http://localhost:2380";
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 超时时间（单位 ：毫秒）
     */
    private Long timeout = 10000L;
}
