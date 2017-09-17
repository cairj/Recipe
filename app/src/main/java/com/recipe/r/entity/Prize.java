package com.recipe.r.entity;

import java.util.ArrayList;

/**
 * Created by hj on 2017/6/20.
 * 我的奖品实体类
 */
public class Prize {
    private int status;
    private String info;
    private ArrayList<Gift> data;

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

    public ArrayList<Gift> getData() {
        return data;
    }

    public void setData(ArrayList<Gift> data) {
        this.data = data;
    }

    public class Gift {
        private String lucky_id;
        private String user_id;
        private String goods_id;//产品id
        private String goods_name;//产品名称
        private String goods_price;//产品价格
        private String goods_number;//产品数量
        private String time;//中奖时间
        private String status;

        public String getLucky_id() {
            return lucky_id;
        }

        public void setLucky_id(String lucky_id) {
            this.lucky_id = lucky_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
        }

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public String getGoods_price() {
            return goods_price;
        }

        public void setGoods_price(String goods_price) {
            this.goods_price = goods_price;
        }

        public String getGoods_number() {
            return goods_number;
        }

        public void setGoods_number(String goods_number) {
            this.goods_number = goods_number;
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
    }

}
