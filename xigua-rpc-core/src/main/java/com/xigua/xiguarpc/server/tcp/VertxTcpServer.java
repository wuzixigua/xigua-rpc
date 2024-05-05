package com.xigua.xiguarpc.server.tcp;

import com.xigua.xiguarpc.server.HttpServer;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import io.vertx.core.parsetools.RecordParser;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VertxTcpServer implements HttpServer {

    @Override
    public void doStart(int port) {
        //创建Vertx实例
        Vertx vertx = Vertx.vertx();

        //创建TCP服务器
        NetServer server = vertx.createNetServer();

        //处理请求
        /*server.connectHandler(socket->{
            //处理连接
            socket.handler(buffer->{
                //处理接收到的字节数组
                byte[] requestData = buffer.getBytes();
                //在这里进行自定义的字节数组处理逻辑，比如解析请求、调用服务、构造相应等
                byte[] responseData = handleRequest(requestData);
                //发送响应
                socket.write(Buffer.buffer(responseData));
            });
        });*/
        server.connectHandler(new TcpServerHandler());

        //启动TCP服务器并监听指定端口
        server.listen(port,result->{
            if(result.succeeded()){
                System.out.println("TCP server started on port "+port);
            }else{
                System.err.println("Failed to start TCP server "+result.cause());
            }
        });
    }
}
