package com.recipe.r.entity;

import java.util.ArrayList;

/**
 * Created by hj on 2017/6/16.
 * 关于推荐的实体类
 */

public class Recommend {
    private int status;
    private String info;
    private ArrayList<Goods> data;

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

    public ArrayList<Goods> getData() {
        return data;
    }

    public void setData(ArrayList<Goods> data) {
        this.data = data;
    }

    public class Goods {
        private String goods_name;
        private String view_num;
        private String shop_price;
        private String goods_image;
        private String sold_num;
        private String make_time;
        private String goods_id;
        private String goods_brief;
        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public String getView_num() {
            return view_num;
        }

        public void setView_num(String view_num) {
            this.view_num = view_num;
        }

        public String getShop_price() {
            return shop_price;
        }

        public void setShop_price(String shop_price) {
            this.shop_price = shop_price;
        }

        public String getGoods_image() {
            return goods_image;
        }

        public void setGoods_image(String goods_image) {
            this.goods_image = goods_image;
        }

        public String getSold_num() {
            return sold_num;
        }

        public void setSold_num(String sold_num) {
            this.sold_num = sold_num;
        }

        public String getMake_time() {
            return make_time;
        }

        public void setMake_time(String make_time) {
            this.make_time = make_time;
        }

        public String getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
        }

        public String getGoods_brief() {
            return goods_brief;
        }

        public void setGoods_brief(String goods_brief) {
            this.goods_brief = goods_brief;
        }
    }

}
