package com.xigua.example.common.model;

import java.io.Serializable;

/**
 * 用户类 实现序列化接口,为后续网络传输序列化提供者支持
 */

public class User implements Serializable {

    private String name;

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }
}
