package com.xigua.xiguarpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.xigua.xiguarpc.RpcApplication;
import com.xigua.xiguarpc.config.RpcConfig;
import com.xigua.xiguarpc.constant.RpcConstant;
import com.xigua.xiguarpc.fault.retry.RetryStrategy;
import com.xigua.xiguarpc.fault.retry.RetryStrategyFactory;
import com.xigua.xiguarpc.fault.tolerant.TolerantStrategy;
import com.xigua.xiguarpc.fault.tolerant.TolerantStrategyFactory;
import com.xigua.xiguarpc.loadbalancer.LoadBalancer;
import com.xigua.xiguarpc.loadbalancer.LoadBalancerFactory;
import com.xigua.xiguarpc.model.RpcRequest;
import com.xigua.xiguarpc.model.RpcResponse;
import com.xigua.xiguarpc.model.ServiceMetaInfo;
import com.xigua.xiguarpc.protocol.*;
import com.xigua.xiguarpc.registry.Registry;
import com.xigua.xiguarpc.registry.RegistryFactory;
import com.xigua.xiguarpc.serializer.Serializer;
import com.xigua.xiguarpc.serializer.SerializerFactory;
import com.xigua.xiguarpc.server.tcp.VertxTcpClient;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 服务代理(JDK动态代理)
 * 动态代理的作用是，根据要生成的对象的类型，自动生成一个代理对象
 * 常用的动态代理实现方式有 JDK 动态代理和基于字节码生成的动态代理（比如 CGLIB）。
 * 前者简单易用、无需引入额外的库，但缺点是只能对接口进行代理；
 * 后者更灵活、可以对任何类进行代理，但性能略低于 JDK 动态代理。
 */
public class ServiceProxy implements InvocationHandler {
    /**
     * 调用代理
     *
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //指定序列化器
//        Serializer serializer = new JdkSerializer();
//        final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());


        //构造请求
        String serviceName = method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(serviceName) //method.getDeclaringClass()获取当前方法所在的类的Class对象
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();

        try {
            //从注册中心获取服务提供者请求地址
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            if (CollUtil.isEmpty(serviceMetaInfoList)) {
                throw new RuntimeException("暂无服务地址");
            }
            //负载均衡
            LoadBalancer loadBalancer = LoadBalancerFactory.getInstance(rpcConfig.getLoadBalancer());
            //将调用方法名（请求路径）作为负载均衡参数
            Map<String,Object> requestParams = new HashMap<>();
            requestParams.put("methodName",rpcRequest.getMethodName());
            ServiceMetaInfo selectedServiceMetaInfo = loadBalancer.select(requestParams, serviceMetaInfoList);

            //发送TCP请求
            // 使用重试机制
            RpcResponse rpcResponse;
            try {
                RetryStrategy retryStrategy = RetryStrategyFactory.getInstance(rpcConfig.getRetryStrategy());
                rpcResponse = retryStrategy.doRetry(() ->
                        VertxTcpClient.doRequest(rpcRequest, selectedServiceMetaInfo)
                );
            } catch (Exception e) {
                //容错机制
                System.out.println("容错执行");
                TolerantStrategy tolerantStrategy = TolerantStrategyFactory.getInstance(rpcConfig.getTolerantStrategy());
                rpcResponse = tolerantStrategy.doTolerant(null, e);
            }
            return rpcResponse.getData();

            /*//发送请求
            try (HttpResponse httpResponse = HttpRequest.post(selectedServiceMetaInfo.getServiceAddress())
                    .body(bodyBytes)
                    .execute()) {
                byte[] result = httpResponse.bodyBytes();
                //反序列化
                RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
                return rpcResponse.getData();
            }*/

        } catch (Exception e) {
            throw new RuntimeException("调用失败");
        }
    }
}
