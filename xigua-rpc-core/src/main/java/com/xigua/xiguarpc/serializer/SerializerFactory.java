package com.xigua.xiguarpc.serializer;

import com.xigua.xiguarpc.utils.SpiLoader;

/**
 * 序列化器工厂  (用于获取序列化器对象) 工厂模式+单例模式
 *  序列化器对象是可以复用的，没必要每次执行序列化操作前都创建一个新的对象
 */
public class SerializerFactory {

    /**
     * 序列化映射 (用于实现单例)
     */
    /*private static final Map<String,Serializer> KEY_SERIALIZER_MAP = new HashMap<String,Serializer>(){
        {
            put(SerializerKeys.JDK,new JdkSerializer());
            put(SerializerKeys.JSON,new JsonSerializer());
            put(SerializerKeys.KRYO,new KryoSerializer());
            put(SerializerKeys.HESSIAN,new HessianSerializer());
        }
    };*/

    /**
     * 之前是硬编码 修改为从SPI加载指定的序列化器对象
     */
    static{
        SpiLoader.load(Serializer.class);
    }

    /**
     * 默认序列化器
     */
    private static final Serializer DEFAULT_SERIALIZER = new JdkSerializer();

    /**
     * 获取实例
     * @param key
     * @return
     */
    public static Serializer getInstance(String key){
        return SpiLoader.getInstance(Serializer.class,key);
    }

}
