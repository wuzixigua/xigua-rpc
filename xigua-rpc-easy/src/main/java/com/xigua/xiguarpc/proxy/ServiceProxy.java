package com.xigua.xiguarpc.proxy;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.xigua.xiguarpc.model.RpcRequest;
import com.xigua.xiguarpc.model.RpcResponse;
import com.xigua.xiguarpc.serializer.JdkSerializer;
import com.xigua.xiguarpc.serializer.Serializer;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 服务代理(JDK动态代理)
 *  动态代理的作用是，根据要生成的对象的类型，自动生成一个代理对象
 *  常用的动态代理实现方式有 JDK 动态代理和基于字节码生成的动态代理（比如 CGLIB）。
 *  前者简单易用、无需引入额外的库，但缺点是只能对接口进行代理；
 *  后者更灵活、可以对任何类进行代理，但性能略低于 JDK 动态代理。
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
        Serializer serializer = new JdkSerializer();

        //构造请求
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(method.getDeclaringClass().getName()) //method.getDeclaringClass()获取当前方法所在的类的Class对象
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();

        try {
            //序列化
            byte[] bodyBytes = serializer.serialize(rpcRequest);
            //发送请求
            //todo:注意, 这里的地址被硬编码了(需要使用注册中心和服务发现机制解决)
            try(HttpResponse httpResponse = HttpRequest.post("http://localhost:8080")
                        .body(bodyBytes)
                        .execute()){
                byte[] result = httpResponse.bodyBytes();
                //反序列化
                RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
                return rpcResponse.getData();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }
}
