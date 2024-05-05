package com.xigua.xiguarpc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * RPC请求类   请求类 RpcRequest 的作用是封装调用所需的信息，
 * 比如服务名称、方法名称、调用参数的类型列表、参数列表。这些都是 Java 反射机制所需的参数。
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RpcRequest implements Serializable {
    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 参数类型列表
     */
    private Class<?>[] parameterTypes;

    /**
     * 参数列表篇
     */
    private Object[] args;

}
