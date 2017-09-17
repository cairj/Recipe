package com.recipe.r.events;

/**
 * 2017
 * 06
 * 2017/6/8
 * wangxiaoer
 * 功能描述：EventBus消息基类
 **/
public class MessageEvent {
    private String message;

    public MessageEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
