package com.recipe.r.entity;

import java.util.ArrayList;

/**
 * Created by hj on 2017/7/8.
 * 收藏实体类
 */
public class CollectionItem {
    private int status;
    private String info;
    private ArrayList<Collecyion> data;

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

    public ArrayList<Collecyion> getData() {
        return data;
    }

    public void setData(ArrayList<Collecyion> data) {
        this.data = data;
    }

    public class Collecyion {
        private String collect_id;
        private String user_id;
        private String goods_id;
        private String time;
        private String status;
        private String goods_name;
        private String shop_price;
        private String collect_num;
        private String collect_content;
        private String goods_image;
        private String goods_summary;

        public String getCollect_id() {
            return collect_id;
        }

        public void setCollect_id(String collect_id) {
            this.collect_id = collect_id;
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

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public String getShop_price() {
            return shop_price;
        }

        public void setShop_price(String shop_price) {
            this.shop_price = shop_price;
        }

        public String getCollect_num() {
            return collect_num;
        }

        public void setCollect_num(String collect_num) {
            this.collect_num = collect_num;
        }

        public String getCollect_content() {
            return collect_content;
        }

        public void setCollect_content(String collect_content) {
            this.collect_content = collect_content;
        }

        public String getGoods_image() {
            return goods_image;
        }

        public void setGoods_image(String goods_image) {
            this.goods_image = goods_image;
        }

        public String getGoods_summary() {
            return goods_summary;
        }

        public void setGoods_summary(String goods_summary) {
            this.goods_summary = goods_summary;
        }
    }

}
