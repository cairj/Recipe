package com.recipe.r.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by hj on 2017/6/16.
 * 创建最新美食实体类
 */
public class NewGoods {
    private int status;
    private String info;
    private ArrayList<Goods> data;
    private int count;

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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "NewGoods{" +
                "status=" + status +
                ", info='" + info + '\'' +
                ", data=" + data +
                ", count=" + count +
                '}';
    }

    public class Goods {
        private String make_time;
        private String goods_name;
        private String purchase;
        private String promote_price;
        private String goods_weight;
        private String taste_id;
        private String goods_brief;
        private String shop_price;
        private String user_id;
        private String goods_cost;
        private String digg_num;
        private String small_img;
        private String parent_id;
        private String goods_sn;
        private String seckill_price;
        private String supplier_id;
        private String shop_id;
        private int status;
        private String sold_num;
        private String parent_ids;
        private String brand_id;
        private String cate_id;
        private String view_num;
        private String goods_image;
        private String goods_tiaoma;
        private String alias_name;
        private String goods_detail;
        private String market_price;
        private String end_time;
        private String goods_id;
        private String is_tuijian;
        private String start_time;
        private int collect_num;
        private String add_time;
        private String sold_month;
        private int goods_number;
        private String goods_summary;

        @Override
        public String toString() {
            return "Goods{" +
                    "make_time='" + make_time + '\'' +
                    ", goods_name='" + goods_name + '\'' +
                    ", purchase='" + purchase + '\'' +
                    ", promote_price='" + promote_price + '\'' +
                    ", goods_weight='" + goods_weight + '\'' +
                    ", taste_id='" + taste_id + '\'' +
                    ", goods_brief='" + goods_brief + '\'' +
                    ", shop_price='" + shop_price + '\'' +
                    ", user_id='" + user_id + '\'' +
                    ", goods_cost='" + goods_cost + '\'' +
                    ", digg_num='" + digg_num + '\'' +
                    ", small_img='" + small_img + '\'' +
                    ", parent_id='" + parent_id + '\'' +
                    ", goods_sn='" + goods_sn + '\'' +
                    ", seckill_price='" + seckill_price + '\'' +
                    ", supplier_id='" + supplier_id + '\'' +
                    ", shop_id='" + shop_id + '\'' +
                    ", status=" + status +
                    ", sold_num='" + sold_num + '\'' +
                    ", parent_ids='" + parent_ids + '\'' +
                    ", brand_id='" + brand_id + '\'' +
                    ", cate_id='" + cate_id + '\'' +
                    ", view_num='" + view_num + '\'' +
                    ", goods_image='" + goods_image + '\'' +
                    ", goods_tiaoma='" + goods_tiaoma + '\'' +
                    ", alias_name='" + alias_name + '\'' +
                    ", goods_detail='" + goods_detail + '\'' +
                    ", market_price='" + market_price + '\'' +
                    ", end_time='" + end_time + '\'' +
                    ", goods_id='" + goods_id + '\'' +
                    ", is_tuijian='" + is_tuijian + '\'' +
                    ", start_time='" + start_time + '\'' +
                    ", collect_num=" + collect_num +
                    ", add_time='" + add_time + '\'' +
                    ", sold_month='" + sold_month + '\'' +
                    ", goods_number=" + goods_number +
                    ", goods_summary='" + goods_summary + '\'' +
                    '}';
        }

        public String getMake_time() {
            return make_time;
        }

        public void setMake_time(String make_time) {
            this.make_time = make_time;
        }

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public String getPurchase() {
            return purchase;
        }

        public void setPurchase(String purchase) {
            this.purchase = purchase;
        }

        public String getPromote_price() {
            return promote_price;
        }

        public void setPromote_price(String promote_price) {
            this.promote_price = promote_price;
        }

        public String getGoods_weight() {
            return goods_weight;
        }

        public void setGoods_weight(String goods_weight) {
            this.goods_weight = goods_weight;
        }

        public String getTaste_id() {
            return taste_id;
        }

        public void setTaste_id(String taste_id) {
            this.taste_id = taste_id;
        }

        public String getGoods_brief() {
            return goods_brief;
        }

        public void setGoods_brief(String goods_brief) {
            this.goods_brief = goods_brief;
        }

        public String getShop_price() {
            return shop_price;
        }

        public void setShop_price(String shop_price) {
            this.shop_price = shop_price;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getGoods_cost() {
            return goods_cost;
        }

        public void setGoods_cost(String goods_cost) {
            this.goods_cost = goods_cost;
        }

        public String getDigg_num() {
            return digg_num;
        }

        public void setDigg_num(String digg_num) {
            this.digg_num = digg_num;
        }

        public String getSmall_img() {
            return small_img;
        }

        public void setSmall_img(String small_img) {
            this.small_img = small_img;
        }

        public String getParent_id() {
            return parent_id;
        }

        public void setParent_id(String parent_id) {
            this.parent_id = parent_id;
        }

        public String getGoods_sn() {
            return goods_sn;
        }

        public void setGoods_sn(String goods_sn) {
            this.goods_sn = goods_sn;
        }

        public String getSeckill_price() {
            return seckill_price;
        }

        public void setSeckill_price(String seckill_price) {
            this.seckill_price = seckill_price;
        }

        public String getSupplier_id() {
            return supplier_id;
        }

        public void setSupplier_id(String supplier_id) {
            this.supplier_id = supplier_id;
        }

        public String getShop_id() {
            return shop_id;
        }

        public void setShop_id(String shop_id) {
            this.shop_id = shop_id;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getSold_num() {
            return sold_num;
        }

        public void setSold_num(String sold_num) {
            this.sold_num = sold_num;
        }

        public String getParent_ids() {
            return parent_ids;
        }

        public void setParent_ids(String parent_ids) {
            this.parent_ids = parent_ids;
        }

        public String getBrand_id() {
            return brand_id;
        }

        public void setBrand_id(String brand_id) {
            this.brand_id = brand_id;
        }

        public String getCate_id() {
            return cate_id;
        }

        public void setCate_id(String cate_id) {
            this.cate_id = cate_id;
        }

        public String getView_num() {
            return view_num;
        }

        public void setView_num(String view_num) {
            this.view_num = view_num;
        }

        public String getGoods_image() {
            return goods_image;
        }

        public void setGoods_image(String goods_image) {
            this.goods_image = goods_image;
        }

        public String getGoods_tiaoma() {
            return goods_tiaoma;
        }

        public void setGoods_tiaoma(String goods_tiaoma) {
            this.goods_tiaoma = goods_tiaoma;
        }

        public String getAlias_name() {
            return alias_name;
        }

        public void setAlias_name(String alias_name) {
            this.alias_name = alias_name;
        }

        public String getGoods_detail() {
            return goods_detail;
        }

        public void setGoods_detail(String goods_detail) {
            this.goods_detail = goods_detail;
        }

        public String getMarket_price() {
            return market_price;
        }

        public void setMarket_price(String market_price) {
            this.market_price = market_price;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
        }

        public String getIs_tuijian() {
            return is_tuijian;
        }

        public void setIs_tuijian(String is_tuijian) {
            this.is_tuijian = is_tuijian;
        }

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public int getCollect_num() {
            return collect_num;
        }

        public void setCollect_num(int collect_num) {
            this.collect_num = collect_num;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }

        public String getSold_month() {
            return sold_month;
        }

        public void setSold_month(String sold_month) {
            this.sold_month = sold_month;
        }

        public int getGoods_number() {
            return goods_number;
        }

        public void setGoods_number(int goods_number) {
            this.goods_number = goods_number;
        }

        public String getGoods_summary() {
            return goods_summary;
        }

        public void setGoods_summary(String goods_summary) {
            this.goods_summary = goods_summary;
        }
    }
}
