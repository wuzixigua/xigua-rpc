package com.xigua.xiguarpc.springboot.starter.annotation;

import com.xigua.xiguarpc.constant.RpcConstant;
import com.xigua.xiguarpc.fault.retry.RetryStrategyKeys;
import com.xigua.xiguarpc.fault.tolerant.TolerantStrategyKeys;
import com.xigua.xiguarpc.loadbalancer.LoadBalancer;
import com.xigua.xiguarpc.loadbalancer.LoadBalancerKeys;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 服务消费者注解（用于注入服务）
 *  需要指定调用服务相关的属性，
 *      比如服务接口类（可能存在多个接口）、版本号、负载均衡器、重试策略、是否mock调用等
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcReference {

    /**
     * 服务接口类
     * @return
     */
    Class<?> interfaceClass() default void.class;

    /**
     * 版本
     * @return
     */
    String serviceVersion() default RpcConstant.DEFAULT_SERVICE_VERSION;

    /**
     * 负载均衡器
     * @return
     */
    String loadBalancer() default LoadBalancerKeys.ROUND_ROBIN;

    /**
     * 重试策略
     * @return
     */
    String retryStrategy() default RetryStrategyKeys.NO;

    /**
     * 容错策略
     * @return
     */
    String tolerantStrategy() default TolerantStrategyKeys.FAIL_FAST;

    /**
     * 模拟调用
     * @return
     */
    boolean mock() default false;
}
