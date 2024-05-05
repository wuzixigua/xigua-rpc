package com.xigua.xiguarpc.serializer;

import java.io.*;

/**
 * 基于java自带的序列化器实现JdkSerializer
 */
public class JdkSerializer implements Serializer{
    /**
     * 序列化
     * @param object
     * @return
     * @param <T>
     * @throws IOException
     */
    @Override
    public <T> byte[] serialize(T object) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(object);
        objectOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * 反序列化
     * @param bytes
     * @param type
     * @return
     * @param <T>
     */
    @Override
    public <T> T deserialize(byte[] bytes, Class<T> type) throws IOException{
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        try {
            return (T)objectInputStream.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }finally {
            objectInputStream.close();
        }
    }
}
