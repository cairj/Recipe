package com.recipe.r.events;

/**
 * 2017
 * 08
 * 2017/8/4
 * wangxiaoer
 * 功能描述：
 **/
public class MainEvent {
    private String message;

    public MainEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
