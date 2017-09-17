package com.recipe.r.entity;

/**
 * Created by hj on 2017/6/10.
 * 验证码
 */
public class CodeModel {
    private String code;
    private int status;
    private String info;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
