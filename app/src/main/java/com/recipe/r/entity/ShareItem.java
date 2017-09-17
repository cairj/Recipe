package com.recipe.r.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


/**
 * 2017
 * 06
 * 2017/6/12
 * wangxiaoer
 * 功能描述：分享圈实体
 **/
public class ShareItem {
    private int status;
    private String info;
    private ArrayList<Sharedata> data;

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

    public ArrayList<Sharedata> getData() {
        return data;
    }

    public void setData(ArrayList<Sharedata> data) {
        this.data = data;
    }

    public class Sharedata {
        private String summary;//总结
        private String news_id;
        private String time;
        private String title;
        private String keywords;//关键字
        private String source;//来源
        private String user_name;//用户名
        private String status;
        private ArrayList<String> images;//图片数组
        private String admin_id;//管理员ID
        private String view_num;
        private String type;
        private String digg_num;//点赞数
        private String user_id;//用户id
        private String headimgurl;//用户头像

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getNews_id() {
            return news_id;
        }

        public void setNews_id(String news_id) {
            this.news_id = news_id;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getKeywords() {
            return keywords;
        }

        public void setKeywords(String keywords) {
            this.keywords = keywords;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getHeadimgurl() {
            return headimgurl;
        }

        public void setHeadimgurl(String headimgurl) {
            this.headimgurl = headimgurl;
        }

        public ArrayList<String> getImages() {
            return images;
        }

        public void setImages(ArrayList<String> images) {
            this.images = images;
        }

        public String getAdmin_id() {
            return admin_id;
        }

        public void setAdmin_id(String admin_id) {
            this.admin_id = admin_id;
        }

        public String getView_num() {
            return view_num;
        }

        public void setView_num(String view_num) {
            this.view_num = view_num;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDigg_num() {
            return digg_num;
        }

        public void setDigg_num(String digg_num) {
            this.digg_num = digg_num;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }
    }
}
