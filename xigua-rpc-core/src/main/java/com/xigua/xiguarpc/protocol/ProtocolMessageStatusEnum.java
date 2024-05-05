package com.xigua.xiguarpc.protocol;

import lombok.Getter;

/**
 * 协议消息状态的枚举
 *  暂时只定义成功、请求失败、响应失败三种枚举值
 */
@Getter
public enum ProtocolMessageStatusEnum {

    OK("ok",20),
    BAD_REQUEST("badRequest",40),
    BAD_RESPONSE("badResponse",50);

    private final String text;

    private final int value;

    ProtocolMessageStatusEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    public static ProtocolMessageStatusEnum getEnumByValue(int value) {
        for (ProtocolMessageStatusEnum anEnum : ProtocolMessageStatusEnum.values()) {
            if (anEnum.getValue() == value) {
                return anEnum;
            }
        }
        return null;
    }
}
