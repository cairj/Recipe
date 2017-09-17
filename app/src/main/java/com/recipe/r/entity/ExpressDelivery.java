package com.recipe.r.entity;

/**
 * 2017/7/31
 * wangxiaoer
 * 功能描述：快递选择类
 **/
public class ExpressDelivery {
    private String method;
    private boolean isClick;

    public ExpressDelivery() {
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
    }
}
