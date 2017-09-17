package com.recipe.r.entity;

import java.util.ArrayList;

/**
 * 2017/7/12
 * wangxiaoer
 * 功能描述：
 **/
public class Comment {
    private int status;
    private String info;
    private ArrayList<Item> data;

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

    public ArrayList<Item> getData() {
        return data;
    }

    public void setData(ArrayList<Item> data) {
        this.data = data;
    }

    public class Item {
        private String content;
        private String user_name;
        private String comment_id;
        private String time;
        private String status;
        private String item_id;
        private String user_id;
        private String headimgurl;
        private String type;
        private String digg_num;
        private ArrayList<Reply> reply;

        @Override
        public String toString() {
            return "Item{" +
                    "content='" + content + '\'' +
                    ", user_name='" + user_name + '\'' +
                    ", comment_id='" + comment_id + '\'' +
                    ", time='" + time + '\'' +
                    ", status='" + status + '\'' +
                    ", item_id='" + item_id + '\'' +
                    ", user_id='" + user_id + '\'' +
                    ", headimgurl='" + headimgurl + '\'' +
                    ", type='" + type + '\'' +
                    ", digg_num='" + digg_num + '\'' +
                    ", reply=" + reply +
                    '}';
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getComment_id() {
            return comment_id;
        }

        public void setComment_id(String comment_id) {
            this.comment_id = comment_id;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getItem_id() {
            return item_id;
        }

        public void setItem_id(String item_id) {
            this.item_id = item_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getHeadimgurl() {
            return headimgurl;
        }

        public void setHeadimgurl(String headimgurl) {
            this.headimgurl = headimgurl;
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

        public ArrayList<Reply> getReply() {
            return reply;
        }

        public void setReply(ArrayList<Reply> reply) {
            this.reply = reply;
        }
    }
    public class Reply{
        private String reply_id;
        private String comment_id;
        private String user_id;
        private String content;
        private long time;
        private int status;
        private String user_name;
        private String headimgurl;

        @Override
        public String toString() {
            return "Reply{" +
                    "reply_id='" + reply_id + '\'' +
                    ", comment_id='" + comment_id + '\'' +
                    ", user_id='" + user_id + '\'' +
                    ", content='" + content + '\'' +
                    ", time=" + time +
                    ", status=" + status +
                    ", user_name='" + user_name + '\'' +
                    ", headimgurl='" + headimgurl + '\'' +
                    '}';
        }

        public String getReply_id() {
            return reply_id;
        }

        public void setReply_id(String reply_id) {
            this.reply_id = reply_id;
        }

        public String getComment_id() {
            return comment_id;
        }

        public void setComment_id(String comment_id) {
            this.comment_id = comment_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
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
    }

    @Override
    public String toString() {
        return "Comment{" +
                "status=" + status +
                ", info='" + info + '\'' +
                ", data=" + data +
                '}';
    }
}
