package com.xigua.xiguarpc.server;

/**
 * Http服务器接口，
 */
public interface HttpServer {
    /**
     * 启动服务器
     */
    void doStart(int port);
}
