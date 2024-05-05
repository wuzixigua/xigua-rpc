package com.xigua.example.consumer;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.xigua.example.common.model.User;
import com.xigua.example.common.service.UserService;
import com.xigua.xiguarpc.model.RpcRequest;
import com.xigua.xiguarpc.model.RpcResponse;
import com.xigua.xiguarpc.serializer.JdkSerializer;
import com.xigua.xiguarpc.serializer.Serializer;

import java.io.IOException;

/**
 * 静态代理 实现 UserService 接口和 getUser 方法
 * 不过实现 getUser 方法时，不是复制粘贴服务提供者 UserServiceImpl 中的代码，
 *  而是要构造 HTTP 请求去调用服务提供者
 *
 *  静态代理虽然很好理解（就是写个实现类嘛），
 *  但缺点也很明显，我们如果要给每个服务接口都写一个实现类，是非常麻烦的，这种代理方式的灵活性很差！
 */
public class UserServiceProxy implements UserService {
    @Override
    public User getUser(User user) {
        //指定序列化器
        Serializer serializer = new JdkSerializer();

        //发请求
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(UserService.class.getName())
                .methodName("getUser")
                .parameterTypes(new Class[]{User.class})
                .args(new Object[]{user})
                .build();

        try {
            byte[] bodyBytes = serializer.serialize(rpcRequest);
            byte[] result;
            try(HttpResponse httpResponse = HttpRequest.post("http://localhost:8080")
                        .body(bodyBytes)
                        .execute()){
                result=httpResponse.bodyBytes();
            }
            RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
            return (User) rpcResponse.getData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
