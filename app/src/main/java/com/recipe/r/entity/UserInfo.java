package com.recipe.r.entity;

import java.util.ArrayList;

/**
 * 2017
 * 07
 * 2017/7/17
 * wangxiaoer
 * 功能描述：
 **/
public class UserInfo {
    private int status;
    private String info;
    private ArrayList<User> data;

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

    public ArrayList<User> getData() {
        return data;
    }

    public void setData(ArrayList<User> data) {
        this.data = data;
    }

    public class User {
        private String headimgurl;
        private String user_name;
        private String user_id;
        private long login_time;

        public String getHeadimgurl() {
            return headimgurl;
        }

        public void setHeadimgurl(String headimgurl) {
            this.headimgurl = headimgurl;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public long getLogin_time() {
            return login_time;
        }

        public void setLogin_time(long login_time) {
            this.login_time = login_time;
        }
    }
}
